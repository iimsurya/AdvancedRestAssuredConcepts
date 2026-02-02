package contracts;

public enum APIResources {

    getPlaceResource("/maps/api/place/get/json"),
    addPlaceResource("/maps/api/place/add/json"),
    deletePlaceResource("/maps/api/place/delete/json");


    private final String resource;

     APIResources (String resource){
        this.resource=resource;
    }

    public String getResource(){
         return resource;
    }
}
