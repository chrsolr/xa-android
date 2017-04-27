package io.keypunchers.xa.models;

public class Achievement {
	private String imageUrl;
	private String title;
	private String description;
	private String gamescoreAmount;
	private String commentAmount;
	private String achievementsPageUrl;
	private boolean isSecret;

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

	public void setAchievementsPageUrl(String achievementsPageUrl) {
		this.achievementsPageUrl = achievementsPageUrl;
	}

	public String getAchievementsPageUrl() {
		return achievementsPageUrl;
	}
}
