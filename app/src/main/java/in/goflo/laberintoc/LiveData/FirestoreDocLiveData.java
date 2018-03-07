package in.goflo.laberintoc.LiveData;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by amisha on 1/3/18.
 */

public class FirestoreDocLiveData extends LiveData<DocumentSnapshot> {

    private static final String TAG = "FirestoreDocLiveData";
    private final DocumentReference documentReference;
    private final MyDocumentSnapshotListener listener = new MyDocumentSnapshotListener();

    public FirestoreDocLiveData(DocumentReference documentReference) {
        this.documentReference = documentReference;
    }

    @Override
    protected void onActive() {
        super.onActive();
        documentReference.get().addOnCompleteListener(listener);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
    }

    private class MyDocumentSnapshotListener implements OnCompleteListener<DocumentSnapshot> {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    setValue(document);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        }
    }
}
