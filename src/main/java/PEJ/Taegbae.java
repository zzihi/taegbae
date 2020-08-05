package PEJ;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Taegbae_table")
public class Taegbae {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String purchaseId;
    private String tbStatus;

    @PostPersist
    public void onPostPersist(){
        //주문
        Tbordered tbordered = new Tbordered();
        BeanUtils.copyProperties(this, tbordered);
        tbordered.setTbStatus("ORDERED");
        tbordered.setTbId(this.getId());
        tbordered.publishAfterCommit();

        PEJ.external.Pay pay = new PEJ.external.Pay();
        // mappings goes here
        pay.setTbId(tbordered.getTbId());
        pay.setPayStatus("PAY");
        System.out.println("11111111111111111111111");
        TaegbaeApplication.applicationContext.getBean(PEJ.external.PayService.class)
            .pay(pay);

    }

    @PreUpdate
    public void onPreUpdate(){

        //취소
        if(this.getTbStatus().equals("CANCELLED")){
            Tbcancelled tbcancelled = new Tbcancelled();
            BeanUtils.copyProperties(this, tbcancelled);
            tbcancelled.setTbId(this.getId());
            tbcancelled.setTbStatus("CANCELLED");
            tbcancelled.publishAfterCommit();
        }

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
    public String getTbStatus() {
        return tbStatus;
    }

    public void setTbStatus(String tbStatus) {
        this.tbStatus = tbStatus;
    }




}
