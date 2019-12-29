package com.example.a20191229_finaltest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_lotto.*
import java.util.*
import kotlin.collections.ArrayList

class LottoActivity : BaseActivity() {

    val winLottoNumArr = ArrayList<Int>()


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
        }

    }

    fun makeWinLottoNum(){

//        6개의 당첨 번호 생성 => 6번 반복을 돌면서 작업
//        랜덤으로 숫자를 생성 => 아무 제약없는 랜덤 X => 1~45의 범위 / 중복 X
//        제약조건을 통과한다면 => 당첨번호 목록으로 추가.(2,10,5) => 배열을 사용
//        작은 숫자부터 나타나도록 정렬
//        여기까지 완료되면 6개의 텍스트뷰에 반영

        for (i in 0..5){

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

    }

    override fun setValues() {

    }
}
