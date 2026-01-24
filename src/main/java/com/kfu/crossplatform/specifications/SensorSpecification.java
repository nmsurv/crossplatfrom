package com.kfu.crossplatform.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.kfu.crossplatform.domain.Sensor;

public class SensorSpecification {
    private static Specification<Sensor> modelLike(String model){
        return (root, query, criterialBuilder) -> {
            if(model==null||model.trim().isEmpty()){return null;}
            return criterialBuilder.like(criterialBuilder.lower(
                root.get("model")), "%" + model.trim().toLowerCase() + "%");
        };
    }

    private static Specification<Sensor>  locationLike(String location){
        return (root, query, criterialBuilder) ->{
            if(location==null||location.trim().isEmpty()){return null;}
            return criterialBuilder.like(criterialBuilder.lower(
                root.get("location")), "%" + location.trim().toLowerCase() + "%");
        };
    }
    public static Specification<Sensor> filter (String model, String location){
        return Specification.allOf(modelLike(model), locationLike(location));
    }
}