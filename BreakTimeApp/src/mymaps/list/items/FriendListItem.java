package mymaps.list.items;

import twitter4j.User;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class FriendListItem implements Parcelable {

    private final User user;
    private Bitmap image;

    public FriendListItem(User user) {

	this.user = user;
    }

    public FriendListItem(User user, Bitmap image) {

	this(user);
	this.image = image;
    }

    public User getUser() {
	return user;
    }

    public Bitmap getImage() {
	return image;
    }

    public void setImage(Bitmap image) {
	this.image = image;
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

    public static final Parcelable.Creator<FriendListItem> CREATOR = new Parcelable.Creator<FriendListItem>() {
	@Override
	public FriendListItem createFromParcel(Parcel in) {
	    return new FriendListItem(in);
	}

	@Override
	public FriendListItem[] newArray(int size) {
	    return new FriendListItem[size];
	}
    };

    private FriendListItem(Parcel in) {
	user = (User) in.readSerializable();
	image = in.readParcelable(Bitmap.class.getClassLoader());
    }
}
