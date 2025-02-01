from flask import Flask, request, jsonify
from kiwipiepy import Kiwi
from collections import defaultdict

# Flask 서버 초기화
app = Flask(__name__)

# Kiwi 형태소 분석기 초기화
kiwi = Kiwi()

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

if __name__ == '__main__':
    # Flask 서버 실행 (localhost:5001)
    app.run(host='0.0.0.0', port=5001, debug=True)
