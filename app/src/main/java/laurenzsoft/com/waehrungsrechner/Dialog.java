package laurenzsoft.com.waehrungsrechner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by Laurenz on 31.08.2016.
 */
public class Dialog extends AlertDialog.Builder {
    public enum ICON {ALERT, INFO};
    public Dialog(Context context, ICON icon, int titleId, int messageId, boolean skipButton, final Accessor handler) {
        super(context);
        this.setIcon((icon == ICON.ALERT) ? android.R.drawable.ic_dialog_alert : android.R.drawable.ic_dialog_info);
        this.setTitle(titleId);
        this.setMessage(messageId);
        if (skipButton) {
            this.setNegativeButton(R.string.ueberspringen, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    handler.setUIState(true);
                }
            });
        }
    }

}
