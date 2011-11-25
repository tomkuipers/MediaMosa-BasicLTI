package nl.uva.mediamosa.basiclti.web.model;

import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.model.LinkType;
import nl.uva.mediamosa.model.MediafileDetailsType;

public class MediaBean {
	private AssetDetailsType assetDetails;
	private LinkType link;
	private MediafileDetailsType mediafileDetails;
	private String player;
	
	public AssetDetailsType getAssetDetails() {
		return assetDetails;
	}
	public void setAssetDetails(AssetDetailsType assetDetails) {
		this.assetDetails = assetDetails;
	}
	public LinkType getLink() {
		return link;
	}
	public void setLink(LinkType link) {
		this.link = link;
	}
	public MediafileDetailsType getMediafileDetails() {
		return mediafileDetails;
	}
	public void setMediafileDetails(MediafileDetailsType mediafileDetails) {
		this.mediafileDetails = mediafileDetails;
	}
	public String getPlayer() {
		return player;
	}
	public void setPlayer(String player) {
		this.player = player;
	}
	
}


