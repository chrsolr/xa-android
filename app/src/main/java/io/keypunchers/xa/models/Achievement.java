package io.keypunchers.xa.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Achievement implements Parcelable {
	private String imageUrl;
	private String title;
	private String description;
	private String gamescoreAmount;
	private String commentAmount;
	private String commentsPageUrl;
	private String gameTitle;
	private boolean isSecret;

	public Achievement() {
	}

	protected Achievement(Parcel in) {
		imageUrl = in.readString();
		title = in.readString();
		description = in.readString();
		gamescoreAmount = in.readString();
		commentAmount = in.readString();
		commentsPageUrl = in.readString();
		gameTitle = in.readString();
		isSecret = in.readByte() != 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(imageUrl);
		dest.writeString(title);
		dest.writeString(description);
		dest.writeString(gamescoreAmount);
		dest.writeString(commentAmount);
		dest.writeString(commentsPageUrl);
		dest.writeString(gameTitle);
		dest.writeByte((byte) (isSecret ? 1 : 0));
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Achievement> CREATOR = new Creator<Achievement>() {
		@Override
		public Achievement createFromParcel(Parcel in) {
			return new Achievement(in);
		}

		@Override
		public Achievement[] newArray(int size) {
			return new Achievement[size];
		}
	};

	public void setIsSecret(boolean isSecrect) {
		this.isSecret = isSecrect;
	}

	public boolean isSecret() {
		return isSecret;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public String getImageUrl() {
		return imageUrl;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setGamescoreAmount(String gamescoreAmount) {
		this.gamescoreAmount = gamescoreAmount;
	}

	public String getGamescoreAmount() {
		return gamescoreAmount;
	}

	public void setCommentAmount(String commentAmount) {
		this.commentAmount = commentAmount;
	}

	public String getCommentAmount() {
		return commentAmount;
	}

	public void setCommentsPageUrl(String commentsPageUrl) {
		this.commentsPageUrl = commentsPageUrl;
	}

	public String getCommentsPageUrl() {
		return commentsPageUrl;
	}

	public String getGameTitle() {
		return gameTitle;
	}

	public void setGameTitle(String gameTitle) {
		this.gameTitle = gameTitle;
	}
}
