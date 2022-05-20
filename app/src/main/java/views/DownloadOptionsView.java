package views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class DownloadOptionsView extends DialogFragment {

    public  interface ChooseOptionListener{
        public void onDialogOptionClick(int which);
    }

    ChooseOptionListener chooseOptionListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            chooseOptionListener = (ChooseOptionListener) context;
        } catch (ClassCastException e) {
            System.out.println(e);
            // The activity doesn't implement the interface, throw exception

        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Способ загрузки игры")
                .setItems(new String[]{"Загрузить файл pgn", "Загурзить по fen"}, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        chooseOptionListener.onDialogOptionClick(which);

                    }
                }).setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }
}
