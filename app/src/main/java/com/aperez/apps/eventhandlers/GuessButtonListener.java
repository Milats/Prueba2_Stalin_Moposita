package com.aperez.apps.eventhandlers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.aperez.apps.androidfunwithflags.MainActivity;
import com.aperez.apps.androidfunwithflags.MainActivityFragment;
import com.aperez.apps.androidfunwithflags.R;
import com.aperez.apps.androidfunwithflags.ResultsDialogFragment;
import com.aperez.apps.data.DatabaseHelper;
import com.aperez.apps.lifecyclehelpers.QuizViewModel;
import com.aperez.apps.login.LoginSIMP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class GuessButtonListener implements OnClickListener {
    private MainActivityFragment mainActivityFragment;
    private Handler handler;
    private Context c;

    public GuessButtonListener(MainActivityFragment mainActivityFragment) {
        this.mainActivityFragment = mainActivityFragment;
        this.handler = new Handler();
        this.c = mainActivityFragment.getContext();
    }

    @Override
    public void onClick(View v) {
        Button guessButton = ((Button) v);
        String guess = guessButton.getText().toString();
        String answer = this.mainActivityFragment.getQuizViewModel().getCorrectCountryName();
        this.mainActivityFragment.getQuizViewModel().setTotalGuesses(1);

        if (guess.equals(answer)) {
            this.mainActivityFragment.getQuizViewModel().setCorrectAnswers(1);
            this.mainActivityFragment.getAnswerTextView().setText(answer + "!");
            this.mainActivityFragment.getAnswerTextView().setTextColor(
                    this.mainActivityFragment.getResources().getColor(R.color.correct_answer));

            this.mainActivityFragment.disableButtons();

            if (this.mainActivityFragment.getQuizViewModel().getCorrectAnswers()
                    == QuizViewModel.getFlagsInQuiz()) {
                ResultsDialogFragment quizResults = new ResultsDialogFragment();
                quizResults.setCancelable(false);
                try {
                    quizResults.show(this.mainActivityFragment.getChildFragmentManager(), "Quiz Results");
                } catch (NullPointerException e) {
                    Log.e(QuizViewModel.getTag(),
                            "GuessButtonListener: this.mainActivityFragment.getFragmentManager() " +
                                    "returned null",
                            e);
                }
                DatabaseHelper SIMPdbHelper = new DatabaseHelper((MainActivity) this.c, "AddressBook.db", null, 1);
                SQLiteDatabase SIMPsql = SIMPdbHelper.getReadableDatabase();
                int level = 1;
                String SIMPconsulta1 = "SELECT ActualLevel " +
                        "FROM POINTS " +
                        "WHERE Player = '" + MainActivity.player + "'";
                Cursor cursor = SIMPsql.rawQuery(SIMPconsulta1, null);
                if (cursor.moveToFirst()){
                    level = Integer.parseInt(cursor.getString(0)) + 1;
                }
                String SIMPconsulta2 = "UPDATE POINTS " +
                        "SET ActualLevel = '" + level + "'" +
                        " WHERE Player = '" + MainActivity.player + "'";
                Cursor cursor2 = SIMPsql.rawQuery(SIMPconsulta2, null);
                SIMPdbHelper.close();

            } else {
                this.handler.postDelayed(
                        new Runnable() {
                            @Override
                            public void run() {
                                mainActivityFragment.animate(true);
                            }
                        }, 2000);
            }
        } else {
            this.mainActivityFragment.incorrectAnswerAnimation();
            guessButton.setEnabled(false);
        }
    }
}
