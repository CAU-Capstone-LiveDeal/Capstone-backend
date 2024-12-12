# README

## 프로젝트 개요 

본 프로젝트는 사용자와 음식점 간의 양방향 소통을 기반으로 상권 활성화를 목표로 하는 할인 정보 제공 플랫폼입니다. 사용자들은 주변 상권의 해피아워, 프로모션을 한 눈에 확인할 수 있으며, 점주들은 플랫폼에 직접 할인 정보를 등록하고, 실시간 혼잡도와 리뷰 데이터에 기반한 매장 분석 제안을 통해 매출 증대를 기대할 수 있습니다. 또한, 주문 시 QR 코드 스캔을 통한 간편한 메뉴 조회 및 주문, 사용자 성향 기반 매장 추천과 AI 분석을 통해 고객-매장 모두에게 이익과 편의를 제공하는 시스템입니다.

### 주요 목표:
- 업장: 비활성 시간대를 활용한 효율적 할인 이벤트 홍보 및 고객 유치
- 고객: 실시간 위치 기반 매장 탐색, 합리적인 할인 혜택, 주문 편의성 제공

## 구현 기능 

1. 할인 정보 제공 및 홍보  
   - 업주는 매장 정보, 메뉴, 할인 이벤트(해피아워, 특정 품목 할인) 등을 등록 가능.  
   - 매장 근처의 고객에게 실시간 할인 안내를 통해 매장 활성화 및 매출 극대화.

2. 실시간 위치 기반 탐색  
   - 고객은 GPS를 통해 주변 매장 정보 및 할인 이벤트 실시간 확인.  
   - 원하는 카테고리(한식, 중식, 양식, 일식)별 매장 탐색 가능.

3. 실시간 혼잡도 제공
   - 매장의 홀 테이블 수 대비 이용 고객 수를 통한 실시간 혼잡도 계산.  

4. AI 분석 기반 추천
   - AI 분석을 통한 매장 진단.
   - 사용자 취향 데이터 기반 맞춤 매장 추천.

5. 주문 및 리뷰 관리  
   - QR 코드 스캔으로 메뉴 확인 및 실시간 주문.  

## 주요 API 정리 

### 인증 및 사용자 관리 (Auth & User)
- 회원가입 및 로그인  
  - `POST /api/register` : 일반 회원가입  
  - `POST /api/register/admin` : 관리자(점주) 회원가입  
  - `POST /api/login` : 로그인 및 JWT 토큰 발급

- 사용자 정보 관리  
  - `PUT /api/users/username` : 사용자 이름 변경  
  - `PUT /api/users/preferences` : 사용자 선호도 변경  
  - `PUT /api/users/password` : 비밀번호 변경  
  - `PUT /api/users/location` : 사용자 위치 업데이트  
  - `GET /api/users/importance` : 사용자 중요도(맛, 인테리어, 청결도, 서비스) 조회  
  - `PUT /api/users/importance` : 사용자 중요도 업데이트  
  - `GET /api/users/me` : 현재 로그인한 사용자 정보 조회

### 매장 관리 (Store)
- `POST /api/stores/register` : 매장 등록  
- `PUT /api/stores/{storeId}` : 매장 정보 수정  
- `PUT /api/stores/{storeId}/update-congestion` : 매장 혼잡도 업데이트  
- `GET /api/stores/{storeId}/congestion` : 특정 매장의 혼잡도 조회  
- `GET /api/stores/nearby` : 사용자 위치 기반 주변 매장 조회  
- `GET /api/stores/my-store-id` : 점주가 등록한 매장 ID 조회

### 리뷰 관리 (Review)
- `POST /api/reviews/{storeId}` : 매장에 리뷰 작성  
- `GET /api/reviews/store/{storeId}` : 특정 매장 리뷰 조회  
- `PUT /api/reviews/{reviewId}` : 리뷰 수정  
- `DELETE /api/reviews/{reviewId}` : 리뷰 삭제

### 주문 관리 (Orders)
- `POST /api/orders` : 주문 생성  
- `GET /api/orders/{orderId}` : 주문 정보 조회  
- `PUT /api/orders/{orderId}` : 주문 정보 수정  
- `DELETE /api/orders/{orderId}` : 주문 취소  
- `GET /api/orders/user` : 사용자의 주문 목록 조회  
- `GET /api/orders/store/{storeId}` : 특정 매장의 주문 목록 조회

### 메뉴 관리 (Menu)
- `POST /api/menus/{storeId}` : 매장에 메뉴 추가  
- `GET /api/menus/store/{storeId}` : 특정 매장 메뉴 조회  
- `GET /api/menus/{menuId}` : 특정 메뉴 상세 조회  
- `PUT /api/menus/{menuId}` : 메뉴 수정  
- `DELETE /api/menus/{menuId}` : 메뉴 삭제

### 추천 및 분석 (Recommendation & Analysis)
- `POST /api/top5recommendations` : 사용자 선호도 기반 탑5 매장 추천  
- `POST /api/recommendationscore` : 리뷰 분석 점수 생성  
- `GET /api/suggestions/store/{storeId}` : 매장 리뷰 기반 추천  
- `GET /api/analysis/store/{storeId}` : 매장 리뷰 분석 결과 조회

### 할인 (Discount)
- `GET /api/discounts` : 모든 할인 정보 조회  
- `POST /api/discounts` : 할인 정보 등록  
- `DELETE /api/discounts/{discountId}` : 할인 정보 삭제

### 혼잡도 (Store Congestion)
- `POST /api/store-congestion/register` : 상권 혼잡도 등록  
- `GET /api/store-congestion/{storeId}/all` : 특정 매장의 혼잡도 이력 조회
