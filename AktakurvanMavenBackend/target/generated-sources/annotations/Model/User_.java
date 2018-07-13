package Model;

import Model.Friendship;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-12-22T19:24:09")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile ListAttribute<User, Friendship> receivedFriendRequest;
    public static volatile ListAttribute<User, Friendship> sentFriendRequests;
    public static volatile SingularAttribute<User, String> email;
    public static volatile SingularAttribute<User, String> deviceToken;

}