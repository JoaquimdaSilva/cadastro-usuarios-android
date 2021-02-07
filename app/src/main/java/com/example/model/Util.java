package com.example.model;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.io.ByteArrayOutputStream;

public class Util {

    @SuppressLint("NewApi")
    public static void onClickShadow(final ImageButton button) {

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent me) {
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    button.setColorFilter(Color.argb(150, 17, 17, 17));
                } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    button.setColorFilter(null); // or null
                }
                return false;
            }
        });

    }

    public static abstract class MaskEditTextDate {

        private static String mask;

        public static String unmask(String s) {
            return s.replaceAll("[.]", "").replaceAll("[-]", "")
                    .replaceAll("[/]", "").replaceAll("[(]", "")
                    .replaceAll("[)]", "");
        }

        public static TextWatcher insert(String pathMask, final EditText ediTxt) {
            mask = pathMask;
            return new TextWatcher() {
                boolean isUpdating;
                String old = "";

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {

                    if (s.length() > 0) {
                        mask = "##/##/####";
                    }

                    String str = MaskEditTextDate.unmask(s.toString());
                    String mascara = "";
                    if (isUpdating) {
                        old = str;
                        isUpdating = false;
                        return;
                    }
                    int i = 0;
                    for (char m : mask.toCharArray()) {
                        if (m != '#' && str.length() > old.length()) {
                            mascara += m;
                            continue;
                        }
                        try {
                            mascara += str.charAt(i);
                        } catch (Exception e) {
                            break;
                        }
                        i++;
                    }
                    isUpdating = true;
                    ediTxt.setText(mascara);
                    ediTxt.setSelection(mascara.length());
                }


                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                public void afterTextChanged(Editable s) {
                }
            };
        }

        public static TextWatcher insert(final EditText ediTxt) {
            return new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                    ediTxt.setText("");
                }

                public void afterTextChanged(Editable s) {
                }
            };
        }

        public static void clearOnFocus(final EditText editText) {
            editText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText("");
                }
            });

        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inDither = false;                     //Disable Dithering mode
        options.inPurgeable = true;                   //Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inInputShareable = true;              //Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future
        options.inTempStorage = new byte[32 * 1024];
        BitmapFactory.decodeFile(resId, options);

        if (reqHeight == 0 && reqWidth == 0) {
            options.inSampleSize = 1;
        }

        return BitmapFactory.decodeFile(resId, options);
    }

    public static String imageToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static Bitmap base64ToBitmap(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return bitmap;
    }
}

