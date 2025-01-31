from flask import Flask, request, send_file
from wordcloud import WordCloud
import matplotlib.pyplot as plt
import io

app = Flask(__name__)

@app.route('/generate-wordcloud', methods=['POST'])
def generate_wordcloud():
    data = request.get_json()
    word_freq = data.get("wordFreq", {})

    wordcloud = WordCloud(font_path='/usr/share/fonts/truetype/nanum/NanumGothic.ttf',
                          background_color="white",
                          width=800, height=400).generate_from_frequencies(word_freq)

    img_io = io.BytesIO()
    plt.figure(figsize=(10, 5))
    plt.imshow(wordcloud, interpolation="bilinear")
    plt.axis("off")
    plt.savefig(img_io, format='png')
    img_io.seek(0)

    return send_file(img_io, mimetype='image/png')

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)