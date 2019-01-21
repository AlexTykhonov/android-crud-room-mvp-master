package tech.alvarez.contacts.listedit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import tech.alvarez.contacts.R;
import tech.alvarez.contacts.utils.Constants;

public class DeleteConfirmFragment extends DialogFragment {

    // create object of DeleteListener
    private ListContract.DeleteListener mListener;



    @NonNull
    @Override
    // method получает Bundle
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final long personId = getArguments().getLong(Constants.PERSON_ID);
        // window dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //текст в окошке
        builder.setTitle(R.string.are_you_sure);
// positive button
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.setConfirm(true, personId);
            }
        });
//negative button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mListener.setConfirm(false, personId);
            }
        });
        return builder.create();
    }

    // в млисенер передаем контекст
    // фрагмент прикрепляется к активити
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (ListContract.DeleteListener) context;
    }
}
