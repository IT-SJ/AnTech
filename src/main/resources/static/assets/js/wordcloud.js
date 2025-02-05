// 워드클라우드 렌더링 함수 정의
function renderWordCloud(wordFrequencies, elementId) {
    const wordArray = Object.entries(wordFrequencies).map(([word, freq]) => [word, freq]);

    const colors = ['#ff6666', '#87ff66', '#6682ff', '#f8afe5', '#ffe866', '#ad66ff', '#66deff', '#ffa866'];

    const canvas = document.getElementById(elementId);
    WordCloud(canvas, {
        list: wordArray,
        gridSize: 9,
        weightFactor: 1,
        fontFamily: 'Jua, sans-serif', // ✅ Jua 폰트 적용
        color: () => colors[Math.floor(Math.random() * colors.length)],
        drawOutOfBound: false,
        shape: 'circle', // 모양
        rotateRatio: 1,  // ✅ 모든 단어가 회전할 수 있도록 설정
        minRotation: 0,  // ✅ 최소 회전 각도 0도
        maxRotation: 10 * (Math.PI / 180),  // ✅ 최대 회전 각도 10도 (라디안 변환)
        hover: function(item, dimension, event) {
            if (dimension) {
                canvas.style.cursor = "pointer";
                document.querySelectorAll('canvas text').forEach(text => {
                    if (text.textContent === item[0]) {
                        text.style.transition = "transform 0.2s ease-in-out";
                        text.style.transform = "scale(1.2)";
                        setTimeout(() => {
                            text.style.transform = "scale(1)";
                        }, 300);
                    }
                });
            }
        },
        mouseout: function() {
            canvas.style.cursor = "default";
        },
        click: function(item) {
            window.location.href = `/search?keyword=${encodeURIComponent(item[0])}`;
        }
    });
}
