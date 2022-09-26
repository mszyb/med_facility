package pl.mszyb.med_facility.entity;

public class ServiceTypeAlreadyAssignedException extends Exception {
    public ServiceTypeAlreadyAssignedException(String message){
        super(message);
    }
}
