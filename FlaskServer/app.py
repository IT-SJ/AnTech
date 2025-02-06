from flask import Flask, request, jsonify
from kiwipiepy import Kiwi
from collections import defaultdict
from transformers import pipeline
import torch
import torch.nn.functional as F  # ✅ 소프트맥스 사용을 위해 추가

# Flask 서버 초기화
app = Flask(__name__)

# Kiwi 형태소 분석기 초기화
kiwi = Kiwi()

# 감정 분석 모델 로드
sentiment_pipeline = pipeline('sentiment-analysis', model="nlptown/bert-base-multilingual-uncased-sentiment")

@app.route('/process-text', methods=['POST'])
def process_text():
    data = request.get_json()
    main_summaries = data.get("main", [])
    breaking_summaries = data.get("breaking", [])
    
    # 단어 빈도 계산
    word_freq = defaultdict(int)
    
    # 주요 뉴스와 속보 뉴스 요약을 합쳐서 처리
    for text in main_summaries + breaking_summaries:
        # 형태소 분석 수행
        tokens = kiwi.tokenize(text)
        for token in tokens:
            # 일반 명사(NNG)와 고유 명사(NNP)만 포함
            if token.tag in ['NNG', 'NNP']:
                word_freq[token.form] += 1

    # 빈도수가 높은 상위 20개 단어 추출
    sorted_word_freq = sorted(word_freq.items(), key=lambda x: x[1], reverse=True)[:20]
    top_word_freq = dict(sorted_word_freq)

    return jsonify(top_word_freq)  # JSON 형태로 반환

# 주요 뉴스 해시태그 추출
@app.route('/extract-main-hashtags', methods=['POST'])
def extract_main_hashtags():
    data = request.get_json()
    main_content = data.get("mainContent", "")

    # 형태소 분석 및 키워드 추출
    tokens = kiwi.tokenize(main_content)
    word_freq = defaultdict(int)
    for token in tokens:
        if token.tag in ['NNG', 'NNP']:  # 일반 명사(NNG), 고유 명사(NNP)만 포함
            word_freq[token.form] += 1

    # 상위 5개의 단어 추출 및 해시태그 변환
    sorted_word_freq = sorted(word_freq.items(), key=lambda x: x[1], reverse=True)[:5]
    hashtags = [f"#{word}" for word, freq in sorted_word_freq]

    return jsonify(hashtags)

# 속보 뉴스 해시태그 추출
@app.route('/extract-breaking-hashtags', methods=['POST'])
def extract_breaking_hashtags():
    data = request.get_json()
    breaking_content = data.get("breakingContent", "")

    # 형태소 분석 및 키워드 추출
    tokens = kiwi.tokenize(breaking_content)
    word_freq = defaultdict(int)
    for token in tokens:
        if token.tag in ['NNG', 'NNP']:
            word_freq[token.form] += 1

    # 상위 5개의 단어 추출 및 해시태그 변환
    sorted_word_freq = sorted(word_freq.items(), key=lambda x: x[1], reverse=True)[:5]
    hashtags = [f"#{word}" for word, freq in sorted_word_freq]

    return jsonify(hashtags)

# 영빈 감정분석 -----------------------------------------
# ✅ 감정 분석 API 추가
@app.route('/analyze-sentiment', methods=['POST'])
def analyze_sentiment():
    """
    뉴스 요약(SMR) 내용을 감정 분석하여 긍정/부정/중립을 반환하는 API
    """
    data = request.get_json()
    text = data.get("text", "").strip()  # ✅ 불필요한 공백 제거

    if not text:
        return jsonify({"error": "❌ 분석할 텍스트가 없습니다."}), 400

    try:
         # ✅ 입력 텍스트 길이 제한 (512 토큰 이하로 자르기)
        max_length = 512
        truncated_text = text[:max_length]

        # ✅ 감정 분석 실행 및 로짓 값 가져오기
        result = sentiment_pipeline(truncated_text, return_all_scores=True)  # ✅ 모든 감정 확률 반환
        scores = result[0]  # 리스트 안에 있는 결과를 추출

        # ✅ 긍정과 부정 점수만 가져오기
        positive_score = scores[2]['score']  # ✅ 긍정
        negative_score = scores[0]['score']  # ✅ 부정

        # ✅ 100 기준으로 정규화 (중립 제외)
        total = positive_score + negative_score
        if total > 0:
            positive = (positive_score / total) * 100
            negative = (negative_score / total) * 100
        else:
            positive = negative = 50.0  # ✅ 모든 값이 0이면 기본값 설정

        # ✅ 감정 확률을 JSON으로 반환
        return jsonify({
            "positive": round(positive, 2),
            "negative": round(negative, 2),
            "sentiment": "POSITIVE" if positive > negative else "NEGATIVE",  # ✅ 가장 높은 감정 선택
            "raw_scores": {score['label']: round(score['score'], 4) for score in scores}
        })
    except Exception as e:
        return jsonify({"error": f"감정 분석 실패: {str(e)}"}), 500
    

if __name__ == '__main__':
    # Flask 서버 실행 (localhost:5000)
    app.run(host='0.0.0.0', port=5000, debug=True)
