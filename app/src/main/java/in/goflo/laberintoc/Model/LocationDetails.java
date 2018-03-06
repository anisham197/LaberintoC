package in.goflo.laberintoc.Model;

/**
 * Created by Anisha Mascarenhas on 06-03-2018.
 */

public class LocationDetails {

    private String locationName, locationID;

    public LocationDetails(String locationName, String locationID) {
        this.locationName = locationName;
        this.locationID = locationID;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getLocationID() {
        return locationID;
    }
}
