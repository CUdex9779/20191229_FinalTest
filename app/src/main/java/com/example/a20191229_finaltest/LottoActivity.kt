package com.example.a20191229_finaltest

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_lotto.*
import java.util.*
import kotlin.collections.ArrayList

class LottoActivity : BaseActivity() {

    var totalWinMoney = 0L //0을 Long타입으로 대입시켜주는 방법 => 일반적인 0은 int타입으로 저장
    var usedMoney = 0L

    val winLottoNumArr = ArrayList<Int>()
    var bonusNumber = 0
    val winLottoNumTextViewList = ArrayList<TextView>()
    val myLottoNumTextViewList = ArrayList<TextView>()


    val mHandler = Handler()
    var isNowBuyingLotto = false
    var isReturnOk = true

    var firstRankCount = 0
    var seconedRankCount = 0
    var thirdRankCount = 0
    var fourthRankCount = 0
    var fifthRankCount = 0
    var noRankCount = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lotto)

        setEvents()
        setValues()
    }

    override fun setEvents() {

        buyOneLottoBtn.setOnClickListener {
//            6개의 숫자를 랜덤으로 생성 => 텍스트뷰 6개에 반영
            makeWinLottoNum()

//            몇등인지 판단하기
            checkLottoRank()

        }

        buyAutoLottoBtn.setOnClickListener {


            if (!isReturnOk){

                Toast.makeText(mContext,"초기화 버튼을 눌러주세요",Toast.LENGTH_SHORT).show()

            }

            else{


                if (!isNowBuyingLotto){
                    buyLottoLoop()
                    isNowBuyingLotto=true
                    buyAutoLottoBtn.text = "자동 구매 일시 정지"
                }
                else{
//                구매 중단
                    mHandler.removeCallbacks(buyingLottoRunnable)
                    isNowBuyingLotto = false
                    buyAutoLottoBtn.text = "자동 구매 재개"
                }
            }



        }

        resetLottoBtn.setOnClickListener {

            resetCount()

        }

    }

    fun buyLottoLoop(){
        mHandler.post(buyingLottoRunnable)
    }

    val buyingLottoRunnable = object : Runnable{
        override fun run() {

            if (usedMoney < 100000000){
                makeWinLottoNum()
                checkLottoRank()
                buyLottoLoop()
            }
            else{
                runOnUiThread {
                    Toast.makeText(mContext,"로또구매를 종료합니다.",Toast.LENGTH_SHORT).show()

                }

                isReturnOk = false
                isNowBuyingLotto = false
                buyAutoLottoBtn.text = "자동 구매 재개"
            }

        }

    }

    fun checkLottoRank(){

//        등수 판단?
//        내가 가진 숫자들과/당첨번호를 하나하나 비교해서 , 같은 숫자가 몇개인지 세어야함.
//        이 갯수에 따라서 등수를 판정
//        갯수가 6개 : 1등 , 5개 : 3등 , 4개 : 4등 , 3개 : 5등
        var correctCount = 0

//        내가 가진 숫자들을 모두 꺼내보자.
//        총 몇개의 숫자를 맞췄는지를 correntCount에 저장
        for (myNumTxt in myLottoNumTextViewList){
//            각 텍스트뷰에 적힌 숫자가 String 형태 -> Int로 변환
            val num = myNumTxt.text.toString().toInt()

            Log.d("적혀있는숫자들",num.toString())

//              당첨번호를 둘러보자
            for (winNum in winLottoNumArr){

//                같은 숫자를 찾았다면
                if (num == winNum){
//                    당첨번호에 들어있다. 갯수 1증가
                    correctCount++
                    break
                }
            }
        }
//        맞춘 갯수에 따라 등수를 판정

        if (correctCount == 6){
//            1등 당첨 => 당첨금액 += 50억
            totalWinMoney += 5000000000
            firstRankCount++
        }
        else if(correctCount ==5){

//            보너스 번호가 맞으면 2등/아니면 3등
            var isSecondRank = false

            for (myNumTxt in myLottoNumTextViewList){
                val myNumber = myNumTxt.text.toString().toInt()

                if (myNumber == bonusNumber){

                    isSecondRank = true

                }
            }
            if (isSecondRank){
//                2등 += 5천만원
                totalWinMoney += 50000000
                seconedRankCount++
            }
            else{
                //            당첨금액 += 150만원

                totalWinMoney += 1500000
                thirdRankCount++
            }

        }
        else if (correctCount ==4){
//            당첨금액 += 5만원
            totalWinMoney += 50000
            fourthRankCount++
        }
        else if (correctCount == 3){
//            5천원
            usedMoney -= 5000
            fifthRankCount++
        }
        else{


            noRankCount++
        }

        totalWinMoneyTxt.text = String.format("%,d 원",totalWinMoney)

        usedMoney += 1000
        totalUseMoneyTxt.text = String.format("%,d 원",usedMoney)

        firstRankCountTxt.text = "${firstRankCount}"
        secondRankCountTxt.text = "${seconedRankCount}"
        thirdRankCountTxt.text = "${thirdRankCount}"
        fourthRankCountTxt.text = "${fourthRankCount}"
        fifthRankCountTxt.text = "${fifthRankCount}"
        noRankCountTxt.text = "${noRankCount}"
    }

    fun makeWinLottoNum(){
//        기존의 당첨번호를 싸그리 삭제
//        6개의 당첨 번호 생성 => 6번 반복을 돌면서 작업
//        랜덤으로 숫자를 생성 => 아무 제약없는 랜덤 X => 1~45의 범위 / 중복 X
//        제약조건을 통과한다면 => 당첨번호 목록으로 추가.(2,10,5) => 배열을 사용
//        작은 숫자부터 나타나도록 정렬
//        여기까지 완료되면 6개의 텍스트뷰에 반영

        winLottoNumArr.clear()
//        기존의 보너스 넘버 초기화
        bonusNumber = 0

//        당첨번호 "6개"를 만들기 위한 for문
        for (i in 0..5){

//            제약 조건을 만족할때까지 무한반복 시키기 위한 while문
            while (true){
                val randomInt = Random().nextInt(45)+1 //0~44의 랜덤값 +1 => 1~45의 랜덤

                var isDupOk = true//중복검사를 통과한다고 먼저 전제

                for (winNum in winLottoNumArr){
                    if (randomInt == winNum){
//                    이미 뽑아둔 당첨번호와 랜덤으로 뽑은 번호가 같다! => 중복

                        isDupOk = false
                        break
                    }
                }

                if (isDupOk){

                    winLottoNumArr.add(randomInt)
                    break
                }
            }


        }

//        Collections 클래스를 통해 sort하는 기능을 사용
        Collections.sort(winLottoNumArr)

//        보너스 번호도 생성
//        1~45값 / 기존의 당첨번호 6개와 중복 X => 1개만

//        보너스 번호가 괜찮을 때까지 무한반복
        while (true){

            val tempNum = Random().nextInt(45)+1

            var isDupOk = true
            for (winNum in winLottoNumArr){
                if (tempNum == winNum){
//                    중복이라 사용안됨을 표시
                    isDupOk = false
                }
            }

            if (isDupOk){
                bonusNumber = tempNum
//                제약조건에 맞는 숫자를 찾았으니 무한반복 정리
                break
            }

        }

        for (i in 0..5){
            val tempTextView = winLottoNumTextViewList.get(i)
            val winNum = winLottoNumArr.get(i)

            tempTextView.text = winNum.toString()


        }

        val bonusTempNum = winLottoNumTextViewList.get(6)

        bonusTempNum.text = bonusNumber.toString()

    }

    fun resetCount(){

        usedMoney = 0
        totalWinMoney = 0
        firstRankCount = 0
        seconedRankCount = 0
        thirdRankCount = 0
        fourthRankCount = 0
        fifthRankCount = 0
        noRankCount = 0

        totalWinMoneyTxt.text = String.format("%,d 원",totalWinMoney)

        totalUseMoneyTxt.text = String.format("%,d 원",usedMoney)

        firstRankCountTxt.text = "${firstRankCount}"
        secondRankCountTxt.text = "${seconedRankCount}"
        thirdRankCountTxt.text = "${thirdRankCount}"
        fourthRankCountTxt.text = "${fourthRankCount}"
        fifthRankCountTxt.text = "${fifthRankCount}"
        noRankCountTxt.text = "${noRankCount}"

        isReturnOk = true

    }

    override fun setValues() {

//        당첨번호 텍스트뷰들을 배열로 담아둠
        winLottoNumTextViewList.add(lottoNumTxt01)
        winLottoNumTextViewList.add(lottoNumTxt02)
        winLottoNumTextViewList.add(lottoNumTxt03)
        winLottoNumTextViewList.add(lottoNumTxt04)
        winLottoNumTextViewList.add(lottoNumTxt05)
        winLottoNumTextViewList.add(lottoNumTxt06)
        winLottoNumTextViewList.add(bonusNumTxt)

//        내가 뽑은 번호 텍스트뷰를 배열로 담아둠
        myLottoNumTextViewList.add(myNumTxt01)
        myLottoNumTextViewList.add(myNumTxt02)
        myLottoNumTextViewList.add(myNumTxt03)
        myLottoNumTextViewList.add(myNumTxt04)
        myLottoNumTextViewList.add(myNumTxt05)
        myLottoNumTextViewList.add(myNumTxt06)

    }
}
