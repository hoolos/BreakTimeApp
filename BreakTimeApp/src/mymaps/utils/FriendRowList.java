package mymaps.utils;

import java.util.List;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import twitter4j.User;

public class FriendRowList implements Parcelable{
	
	private User user;
	private Bitmap image;
	
	public FriendRowList(User user) {
		
		this.user=user;
	}
	
	public FriendRowList(User user,Bitmap image) {
		
		this(user);
		this.image=image;
	}
	
	public User getUser() {
		return user;
	}
	
	public Bitmap getImage() {
		return image;
	}
	public void setImage(Bitmap image){
		this.image=image;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(user);
		dest.writeParcelable(image, flags);
		
	}



    public static final Parcelable.Creator<FriendRowList> CREATOR
            = new Parcelable.Creator<FriendRowList>() {
        public FriendRowList createFromParcel(Parcel in) {
            return new FriendRowList(in);
        }

        public FriendRowList[] newArray(int size) {
            return new FriendRowList[size];
        }
    };
    
    private FriendRowList(Parcel in) {
        user = (User) in.readSerializable();
        image=in.readParcelable(Bitmap.class.getClassLoader());
    }
}
