package PEJ;

import PEJ.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import PEJ.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{

    @Autowired
    TaegbaeRepository taegbaeRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPurchased_Tborder(@Payload Purchased purchased){

        if(purchased.isMe()){
            Taegbae taegbae = new Taegbae();
            taegbae.setPurchaseId(purchased.getPurchaseId());
            taegbae.setTbStatus("ORDERED");
            taegbaeRepository.save(taegbae);
            System.out.println("##### listener wheneverPurchased_Tborder : taegbae.getPurchaseId()=" + taegbae.getPurchaseId());
        }

    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverCancelled_Tbcancel(@Payload Cancelled cancelled){

        if(cancelled.isMe()){
            System.out.println("##### listener Tbcancel : " + cancelled.toJson());
            Optional<Taegbae> taegbaeOptional = taegbaeRepository.findAllByPurchaseId(cancelled.getPurchaseId());
            Taegbae taegbae = taegbaeOptional.get();
            taegbae.setTbStatus(cancelled.getPurchaseStatus());
            System.out.println("7777777777777 taegbae.getPurchaseId()=" + taegbae.getPurchaseId()
                    + " / taegbae.getTbStatus()=" + taegbae.getTbStatus());
            taegbaeRepository.save(taegbae);
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayed_Changestatus(@Payload Payed payed){

        if(payed.isMe()){
            Optional<Taegbae> taegbaeOptional = taegbaeRepository.findById(payed.getTbId());
            Taegbae taegbae = taegbaeOptional.get();
            taegbae.setTbStatus(payed.getPayStatus());
            taegbaeRepository.save(taegbae);
//            System.out.println("##### listener wheneverPayed_Changestatus : " +
//                    "/taegbae.getId=" + taegbae.getId() +
//                    "/taegbae.getPurchaseId=" + taegbae.getPurchaseId() + "/taegbae.getTbStatus=" + taegbae.getTbStatus());
        }
    }

}
