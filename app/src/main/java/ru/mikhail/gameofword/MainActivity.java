package ru.mikhail.gameofword;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.view.View;

public class MainActivity extends AppCompatActivity {


    //Toast.makeText(this, "Could not read this file", Toast.LENGTH_SHORT).show();

    private final static String FILENAME = "note.txt";
    TextView textViewPC;
    TextView textViewUSER;
    EditText editText;
    ArrayList usedWord = new ArrayList();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPC = (TextView) findViewById(R.id.textView_PC);
        editText = (EditText) findViewById(R.id.editTextMy);
        textViewUSER = (TextView) findViewById(R.id.textView_USER);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_open:
                openFile(FILENAME);
                return true;
            case R.id.action_save:
                saveFile(FILENAME);
                return true;
            default:
                return true;
        }
    }


    public void onClickSend(View v) {
        openFile(FILENAME);
    }
        /*switch (v.getId()) {
            case R.id.btnRead:
                //readFile();

                break;
        }*/
    //}

    // Метод для открытия файла
    //private void openFile(String fileName) {
    private StringBuilder openFile(String fileName) {

        StringBuilder buf = null;
        try {
            buf = new StringBuilder();
            //InputStream json = getAssets().open("note.txt");
            InputStream json = getAssets().open(fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));
            String str;


            /**
             * Значение флага
             * 0 - Слова не существует
             * 1 - Слово уже использовалось
             * 2 - Слово существует и не было использовано
             */
            int flag = 0;

            while ((str = in.readLine()) != null) {
                if (str.equalsIgnoreCase(String.valueOf(editText.getText()))) {
                    if (!usedWord()) {
                        textViewUSER.append(editText.getText() + "\n");
                        textViewPC.append("Такое слово уже использовалось.\n" +
                                "Введите другое слово.\n");
                        flag = 1;
                    } else {
                        textViewUSER.append(editText.getText() + "\n");
                        usedWord.add(editText.getText().toString());
                        flag = 2;
                    }
                    //flag = true;
                }
                buf.append(str);
                buf.append('\n');
            }

            in.close();

            //if (flag == false) {
            if (flag == 0) {
                textViewUSER.append(editText.getText() + "\n");
                textViewPC.append("Лууузер! Вот Вы неграмотный пользователь. Нет такого слова.\n" +
                        "Введите другое слово.\n");
            }
            if (flag == 2) {
                answerPC();
            }
            editText.setText("");
            //textViewPC.setText(buf);

            /*nputStream inputStream = openFileInput(fileName);

            //Resources.getSystem().openRawResource(0);

            if (inputStream != null) {

                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                inputStream.close();
                textViewPC.setText(builder.toString());
            }*/
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
        return buf;

    }

    private void answerPC() {

        ArrayList charList = new ArrayList();

        try {
            InputStream json = getAssets().open(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(json, "UTF-8"));


            String str;
            boolean flag = true;
            while ((str = in.readLine()) != null) {
                int lastChar = editText.getText().length() - 1;

                if (str.charAt(0) == editText.getText().charAt(lastChar)) {
                    for (int i = 0; i < usedWord.size(); i++) {
                        if (usedWord.get(i).toString().equalsIgnoreCase(str)) {
                            flag = false;
                        }
                    }
                    if (flag == true) {
                        charList.add(str);
                    }
                }
            }

            if (charList.size() == 0) {
                textViewPC.append("Поздравляю! Вы выигарли.\n");

            } else {
                String randomWord;
                randomWord = (String) charList.get(0 + (int) (Math.random() * charList.size()));
                textViewPC.append(randomWord + "\n");
                usedWord.add(randomWord);
            }
            in.close();
        } catch (UnsupportedEncodingException e) {
            Toast.makeText(this, "Exception Word", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } catch (IOException e) {
            Toast.makeText(this, "Error IO Word", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    private Boolean usedWord() {
        for (int j = 0; j < usedWord.size(); j++) {
            if (usedWord.get(j).toString().equalsIgnoreCase(String.valueOf(editText.getText()))) {
                return false;
            }
        }
        return true;
    }


    // Метод для сохранения файла
    private void saveFile(String fileName) {
        try {
            OutputStream outputStream = openFileOutput(FILENAME, 0);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            osw.write(textViewPC.getText().toString());
            osw.close();
        } catch (Throwable t) {
            Toast.makeText(getApplicationContext(),
                    "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

}
