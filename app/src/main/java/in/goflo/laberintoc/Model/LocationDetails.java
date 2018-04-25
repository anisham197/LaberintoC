package in.goflo.laberintoc.Model;

/**
 * Created by Anisha Mascarenhas on 06-03-2018.
 */

public class LocationDetails {

    private String locationName, locationID;
    private Double latitude, longitude;

    public LocationDetails(String locationName, String locationID, Double latitude, Double longitude) {
        this.locationName = locationName;
        this.locationID = locationID;
        this.latitude = latitude;
        this.longitude = longitude;
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


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
