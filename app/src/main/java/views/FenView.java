package views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import com.example.myapplication.R;

import chessModel.ChessHistory;

public class FenView extends DialogFragment {
    public interface FenDialogListener {
        public void onDialogFenData(String fen);

    }

    FenDialogListener dialogListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dialogListener = (FenDialogListener) context;
        } catch (ClassCastException e) {

        }
    }
//
        EditText fenInput;
        TextView errorText;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the Builder class for convenient dialog construction
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = requireActivity().getLayoutInflater();
//            View view = inflater.inflate(R.layout.fen_input, null);
//            fenInput=view.findViewById(R.id.fen_input);
            View DialogView = inflater.inflate(R.layout.fen_input,null);

            fenInput = DialogView.findViewById(R.id.fen_input_text); // replace it with the correct XML ID
            errorText=DialogView.findViewById(R.id.textViewError);
            builder.setView(DialogView).setTitle("Введите данные в FEN нотации").setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                  dialogInterface.dismiss();
                }
            }).setPositiveButton("Загрузить", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//
                }
            });

            return builder.create();
        }
    @Override
    public void onResume()
    {
        super.onResume();
        final AlertDialog d = (AlertDialog)getDialog();
        if(d != null)
        {
            Button positiveButton = (Button) d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try {
                        if(ChessHistory.checkFen(fenInput.getText().toString())){
                            dialogListener.onDialogFenData(fenInput.getText().toString());
                            d.dismiss();
                        }
                    } catch (Exception e) {
                        errorText.setVisibility(TextView.VISIBLE);
                        errorText.setText(e.getMessage());
//                        fenInput.setError(e.toString());
                    }

                    //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
                }
            });
        }
    }
    }

