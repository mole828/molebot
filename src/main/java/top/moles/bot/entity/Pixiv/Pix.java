package top.moles.bot.entity.Pixiv;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.awt.*;
import java.util.HashMap;

@Data
public class Pix{
    @MongoId
    ObjectId _id;
    Long group;
    Long pid;

    @Override
    public int hashCode(){
        return pid.hashCode();
    }
}
