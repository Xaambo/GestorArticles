package com.example.gestorarticles;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

public class Dialogs {
    public static void showInformacion(Context ctx, String Texto) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        builder.setMessage(Texto);
        builder.setPositiveButton("Aceptar", null);

        builder.show();
    }

    public static void showToast(Context cts, String Texto) {
        Toast.makeText(cts, Texto, Toast.LENGTH_SHORT).show();
    }

    public static void showToastLargo(Context cts, String Texto) {
        Toast.makeText(cts, Texto, Toast.LENGTH_LONG).show();
    }
}