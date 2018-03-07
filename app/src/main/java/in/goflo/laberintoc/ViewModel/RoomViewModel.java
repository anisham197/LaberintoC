package in.goflo.laberintoc.ViewModel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import in.goflo.laberintoc.LiveData.FirestoreQueryLiveData;
import in.goflo.laberintoc.Model.RoomDetails;

/**
 * Created by amisha on 7/3/18.
 */

public class RoomViewModel extends ViewModel {

    private static final String TAG = "RoomViewModel";
    private static final String KEY_NAME = "name";
    private static final String KEY_ROOMS = "rooms";
    private static final String KEY_BUILDINGID = "buildingID";

    private String roomID;
    private static Query roomQuery;
    private FirestoreQueryLiveData liveData;
    private LiveData<RoomDetails> roomLiveData;


    private class Deserializer implements Function<QuerySnapshot, RoomDetails> {
        @Override
        public RoomDetails apply(QuerySnapshot querySnapshot) {
            List<DocumentSnapshot> rooms = querySnapshot.getDocuments();
            RoomDetails roomDetails;
            for(DocumentSnapshot room : rooms){
                if(room.getId().toLowerCase().equals(roomID)) {
                    roomDetails = new RoomDetails(room.get(KEY_NAME).toString(), room.getId(), room.get(KEY_BUILDINGID).toString());
                    Log.d(TAG, "location " + roomDetails.getRoomID() + " " + roomDetails.getRoomName() + " " + roomDetails.getBuildingID());
                    return roomDetails;
                }
            }
            return null;
        }
    }

    @NonNull
    public LiveData<RoomDetails> getRoomLiveData() {
        return roomLiveData;
    }

    public void setRoomID(String ID) {
        roomID = ID;
        roomQuery = FirebaseFirestore.getInstance().collection(KEY_ROOMS);
        liveData = new FirestoreQueryLiveData(roomQuery);
        roomLiveData = Transformations.map(liveData, new RoomViewModel.Deserializer());
    }
}
