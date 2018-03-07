package in.goflo.laberintoc.Model;

/**
 * Created by goflo on 27/2/18.
 */

public class RoomDetails {

    private String roomName, roomID, buildingID;

    public RoomDetails(String roomName, String roomID, String buildingID) {
        this.roomName = roomName;
        this.roomID = roomID;
        this.buildingID = buildingID;
    }

    public String getBuildingID() {
        return buildingID;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomID() {
        return roomID;
    }
}
