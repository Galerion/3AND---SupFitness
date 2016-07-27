package supinfo.com.supfitness.Model;

import java.text.DateFormat;
import java.util.Date;

import supinfo.com.supfitness.Fragment.WeightFragment;


public class Weight {
    private Integer id;
    private String date;
    private Number weight;

    public Weight(){}

    public Integer getId() {
        return id;
    }

    public void setId() {
        if(WeightFragment.listWeight.size() != 0){
            int lastId = WeightFragment.listWeight.get(WeightFragment.listWeight.size()- 1).getId();
            this.id =  lastId + 1 ;
        }else{
            this.id = 1;
        }
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getDate() {
        return date;
    }

    public void setDate() {
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        this.date = currentDateTimeString;
    }
    public  void setDate(String date){
        this.date = date;
    }

    public Number getWeight() {
        return weight;
    }

    public void setWeight(Number weight) {
        this.weight = weight;
    }
}
