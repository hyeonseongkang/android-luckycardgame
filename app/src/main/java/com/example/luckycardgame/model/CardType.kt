package com.example.luckycardgame.model

/*
확장성: 기존 코드를 수정할 필요 없이 새로운 타입을 추가할 수 있다. 새로운 동물 카드 타입이 필요한 경우, CardType을 상속하여 새로운 클래스를 정의하여 사용하면 된다.
코드 가독성: 관련된 타입을 한 곳에서 관리할 수 있기 때문에 코드의 가독성을 높일 수 있다. DogCard, CatCard, CowCard와 같은 카드 타입들이 하나의 파일에 모여 있어 코드를 읽고 이해하기 쉽다.
 */
sealed class CardType {
    abstract val shape: String
}

object DogCard : CardType() {
    override val shape: String = "\uD83D\uDC36" // 유니코드 U+1F436
}

object CatCard : CardType() {
    override val shape: String = "\uD83D\uDC31" // 유니코드 U+1F431
}

object CowCard : CardType() {
    override val shape: String = "\uD83D\uDC2E" // 유니코드 U+1F42E
}