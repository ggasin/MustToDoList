package com.example.musttodolist.singleton

object randomListSingleton {
    var randomList : List<String> = listOf(
        "산책 30분 이상하기","런닝 3km 이상하기",
        "영화관에서 영화 1편 보기",
        "야외 카페 방문하기",
        "카페에서 책 2시간 이상 읽기" ,
        "주변을 산책하며 풍경 사진 3장 이상 찍기" ,
        "장보기" ,
        "공부중인 혹은 공부 하고싶던 내용 2시간 이상 공부 하기" ,
        "헬스장 혹은 근처 공원에서 운동 1시간 이상 하기." ,
        "자전거 타고 10km 이내 목적지 정해서 가기." ,
        "박물관 혹은 미술관, 전시회 방문하기",
        "하루 걸음수 만보 이상 채우기"
    )
    fun getRandomListIndex(beforeRandomIndex : Int) : Int{
        val randomIndex = (0 until randomListSingleton.randomList.size).filter { it != beforeRandomIndex }.random()
        return randomIndex
    }
}