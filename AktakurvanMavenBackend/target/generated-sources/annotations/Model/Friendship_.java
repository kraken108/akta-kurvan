package Model;

import Model.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-22T19:24:09")
@StaticMetamodel(Friendship.class)
public class Friendship_ { 

    public static volatile SingularAttribute<Friendship, User> receivingFriend;
    public static volatile SingularAttribute<Friendship, Boolean> didAccept;
    public static volatile SingularAttribute<Friendship, Long> id;
    public static volatile SingularAttribute<Friendship, User> sendingFriend;
    public static volatile SingularAttribute<Friendship, Boolean> didRespond;

}