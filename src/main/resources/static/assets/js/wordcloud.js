// 워드클라우드 렌더링 함수 정의
function renderWordCloud(wordFrequencies, elementId) {
    // wordFrequencies 객체를 [단어, 빈도수] 형태의 배열로 변환
    const wordArray = Object.entries(wordFrequencies).map(([word, freq]) => [word, freq]);

    // WordCloud2.js 라이브러리를 사용하여 워드클라우드 렌더링
    WordCloud(document.getElementById(elementId), {
        list: wordArray, // 워드클라우드에 표시할 단어와 빈도수 리스트
        gridSize: 8, // 단어 간 간격 (숫자가 작을수록 간격이 좁아짐)
        weightFactor: 2, // 단어 크기 비율 조정
        fontFamily: 'Nanum Gothic', // 폰트 설정 (한글 지원을 위해 나눔고딕 사용)
        color: () => '#' + Math.floor(Math.random() * 16777215).toString(16), // 랜덤 색상 지정
        rotateRatio: 0.5, // 단어 회전 비율 (0.5는 절반 정도 회전)
        click: function(item) {
            // 단어 클릭 시 실행되는 이벤트 처리
            alert(`단어: ${item[0]}, 빈도수: ${item[1]}`); // 클릭된 단어와 빈도수를 알림창으로 표시
            window.open(`https://www.google.com/search?q=${encodeURIComponent(item[0])}`, '_blank'); 
            // 클릭된 단어를 Google에서 검색 (새 탭에서 열기)
        }
    });
}
