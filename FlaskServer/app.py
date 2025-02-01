from flask import Flask, request, jsonify  # Flask 웹 서버 및 JSON 응답 처리
from kiwipiepy import Kiwi  # 형태소 분석 라이브러리
from collections import Counter  # 단어 빈도수 계산을 위한 Counter

# Flask 서버 초기화
app = Flask(__name__)

# Kiwi 형태소 분석기 초기화
kiwi = Kiwi()

@app.route('/process-text', methods=['POST'])
def process_text():
    """
    Spring Boot에서 전달한 뉴스 요약 데이터를 Kiwi로 분석하여 키워드 빈도수를 반환하는 API
    """
    data = request.json  # JSON 데이터 받기
    text = data.get("text", "")  # "text" 키의 값 가져오기

    if not text:
        return jsonify({"error": "No text provided"}), 400  # 에러 처리 (텍스트 없음)

    # Kiwi 형태소 분석 후 명사(NNP, NNG)만 추출
    tokens = [token.form for token in kiwi.tokenize(text) if token.tag.startswith("N")]

    # 키워드 빈도수 계산
    word_freq = Counter(tokens)

    return jsonify(word_freq)  # JSON 형식으로 반환

if __name__ == '__main__':
    # Flask 서버 실행 (localhost:5001)
    app.run(host='0.0.0.0', port=5001, debug=True)
