// package com.kfu.crossplatform.specifications;

// import java.time.LocalDateTime;
// import java.util.List;
// import org.springframework.data.jpa.domain.Specification;

// import com.kfu.crossplatform.enums.StatusType;
// import com.kfu.crossplatform.domain.Alert;
// import com.example.cross_project.model.Sensor;
// import com.example.cross_project.enums.EventType;



// public class AlertSpecification {
//     private static Specification<Alert> sensorLike(Sensor sensor){
//         return (root, query, criterialBuilder) -> {
//             if(sensor == null){return null;}
//             return criterialBuilder.equal(root.get("sensor"), sensor);
//         };
//     }

//     private static Specification<Alert> typeLike(EventType type){
//         return (root, query, criterialBuilder) -> {
//             if(type == null){return null;}
//             return criterialBuilder.equal(root.get("type"),type);
//         };
//     } 
    
//     private static Specification<Alert> timetampLike(LocalDateTime timetamp){
//         return (root, query, criterialBuilder) -> {
//             if(timetamp == null) {return null;}
//             return criterialBuilder.equal(root.get("timetamp"), timetamp);  
//         };
//     }
    
//     private static Specification<Alert> descriptionLike(String description){
//         return (root, query, criterialBuilder) -> {
//             if(description == null) {return null;}
//             return criterialBuilder.equal(root.get("description"), description);  
//         };
//     }

//     private static Specification<Alert> statusLike(StatusType status){
//         return (root, query, criterialBuilder) -> {
//             if(status == null) {return null;}
//             return criterialBuilder.equal(root.get("status"), status);  
//         };
//     }

//     private static Specification<Alert> photourlLike(List<String> photourl){
//         return (root, query, criterialBuilder) -> {
//             if(photourl == null) {return null;}
//             return criterialBuilder.equal(root.get("photourl"), photourl);  
//         };
//     }

//     public static Specification<Alert> filter(Sensor sensor, EventType type, LocalDateTime timetamp, String description, StatusType status, List<String> photourl){
//         return Specification.allOf(
//             sensorLike(sensor),
//             typeLike(type),
//             timetampLike(timetamp),
//             descriptionLike(description),
//             statusLike(status),
//             photourlLike(photourl)
//         );
//     }
// }
