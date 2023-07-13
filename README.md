# 럭키 보드게임

## 1. 게임보드 만들기

- LinearLayout 사용 -> 수직 방향으로 나열되어 있어서 LinearLayout 사용
- 가장 위 레이아웃은 노란색 배경색으로 높이는 44dp
- A-E까지 5개는 모두 같은 높이이며 배경색은 밝은 회색이며, 좌측에 "A", "B" ... "E" 굵은 글자를 50% 투명한 회색으로 표시한다.
- 맨 아래는 위 5개보다 높이가 더 높아야 하고 나머지 영역을 채워야 하며, 색은 진한 회색
- LinearLayout의 orientation을 vertical로 설정한 뒤 "A", "B" ... "E"의 weight는 1 맨 아래 영역은 1.5로 설정

| [결과화면] |
|:-:|
|<img src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/68272971/49fb315d-891a-4199-b8b3-db1766fce9ee" height=600px>|

## 2. 럭키카드 클래스 구현하기

- sealed class 사용해서 CardType 정의
  확장성: 기존 코드를 수정할 필요 없이 새로운 타입을 추가할 수 있다. 새로운 동물 카드 타입이 필요한 경우, CardType을 상속하여 새로운 클래스를 정의하여 사용하면 된다.
  코드 가독성: 관련된 타입을 한 곳에서 관리할 수 있기 때문에 코드의 가독성을 높일 수 있다. DogCard, CatCard, CowCard와 같은 카드 타입들이 하나의 파일에 모여 있어 코드를 읽고 이해하기 쉽다.

| [결과화면] |
|:-:|
|<img src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/68272971/5442da92-50b2-4386-92a7-eeff7bb39728" height=50px>|


## 3. 카드 나눠주기

- 사용자 카드 표시
  RecyclerView 사용해서 사용자 카드 리스트 표시

- 사용자 카드 겹침
  RecyclerView.ItemDecoration() 사용해서 카드 겸침 구현

- A외 다른 사용자 뒷면 표시
  Adapter생성자에 Boolean Type cardOnwer 추가한 뒤 false인 경우 뒷면 표시

### [결과화면]
| 3명 | 4명 | 5명 |
|:-:|:-:|:-:|
|<img src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/68272971/ac4e109d-6796-4c3a-9ce7-f9cf26dc5eb2" height=500px>|<img src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/68272971/f2843c5c-74b8-495f-9466-7899ec91b29a" height=500px>|<img src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/68272971/4451f114-3b38-4678-bb71-f0459eba0515" height=500px>|


## 4. 게임로직 구현하기

### 테스트 케이스
- 참가자 수에 따른 카드 수를 생성하는지 테스트
- 참가자 수에 따른 참가자와 테이블의 카드 수를 설정하는지 테스트
- 참가자와 테이블의 카드를 숫자 오름차순으로 정렬하는지 테스트
- A, B 참가자의 카드 중 가장 낮은 수, 테이블에서 랜덤으로 뽑은 수가 같은지 테스트
- 참가자중 같은 카드 3장 가지고 있는지 테스트
- 참가자가 3명일때 12번째 카드는 생성하지 않는지 테스트

## 5. 게임규칙 추가하기
- 뒤집혀 있는 카드를 터치하면 앞면이 보이도록 카드를 표시한다
- 사용자별로 총 3장의 카드만 뒤집을 수 있다
- 만약 3장의 카드가 숫자가 같으면 표시 화면에서 제거하고, 결과 화면으로 보내버린다.
- 게임이 끝나는 경우는 3장을 모은 카드 숫자 합 또는 차가 7이면 끝난다. 누군가 한 명이 숫자7 3장을 모아도 끝나고, A와 B가 각각 1과 8을 모았어도 끝난다.

## 6. 결과 화면 만들기

- 뽑은 수 합 or 차 7일 경우
- 한 명이 7카드 3장 뽑았을 경우


### [결과 화면]
| 합 or 차 7 | 7뽑았을 때 |
|:-:|:-:|
|<img src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/68272971/0eb8991a-1d72-4c22-857e-2435deed4cae" height=600px>|<img src="https://github.com/softeerbootcamp-2nd/android-luckycardgame/assets/68272971/64f5435e-00db-4f5d-8974-1183403d31e5" height=600px>|

