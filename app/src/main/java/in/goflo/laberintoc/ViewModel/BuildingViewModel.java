package in.goflo.laberintoc.ViewModel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import in.goflo.laberintoc.LiveData.FirestoreDocLiveData;

/**
 * Created by amisha on 7/3/18.
 */

public class BuildingViewModel extends ViewModel {

    private static final String TAG = "BuildingViewModel";
    private static final String KEY_BUILDINGS = "buildings";
    private static final String KEY_NAME = "name";

    private String buildingID;
    private FirestoreDocLiveData liveData;
    private LiveData<String> buildingLiveData;

    private class Deserializer implements Function<DocumentSnapshot, String> {
        @Override
        public String apply(DocumentSnapshot documentSnapshot) {
            Log.d(TAG, "result " + documentSnapshot.getData());
            return documentSnapshot.get(KEY_NAME).toString();
        }
    }

    @NonNull
    public LiveData<String> getBuildingLiveData() {
        return buildingLiveData;
    }


    public void setBuildingID(String ID) {
        buildingID = ID;
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection(KEY_BUILDINGS).document(buildingID);
        liveData = new FirestoreDocLiveData(documentReference);
        buildingLiveData = Transformations.map(liveData, new BuildingViewModel.Deserializer());
    }
}
