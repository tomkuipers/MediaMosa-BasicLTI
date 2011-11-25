package nl.uva.mediamosa.basiclti.web.controller;

import java.io.IOException;

import javax.annotation.Resource;

import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.basiclti.service.BasicLTIResourceService;
import nl.uva.mediamosa.basiclti.web.model.MediaBean;
import nl.uva.mediamosa.model.AssetDetailsType;
import nl.uva.mediamosa.model.LinkType;
import nl.uva.mediamosa.model.MediafileDetailsType;
import nl.uva.mediamosa.util.ServiceException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PlayController {
	
	protected static final Logger logger = Logger.getLogger("PlayController");
	
	@Resource(name="mediamosaSpringService")
	private MediaMosaSpringService mediamosaSpringService;
	
	@Resource(name="btliResourceService")
	private BasicLTIResourceService bltiResourceService;
	
	@RequestMapping(method = RequestMethod.GET, value = "/play/{aId}")
	public @ResponseBody String getPlayer(@PathVariable("aId") String assetId) throws IOException {
		
		MediaMosaService mmService = mediamosaSpringService.getService();
		AssetDetailsType assetDetails;
		MediafileDetailsType mediafileDetails;
		LinkType embedLink;
		try {
			assetDetails = mmService.getAssetDetails(assetId);
			mediafileDetails = assetDetails.getMediafiles().getMediafile().get(0);
			embedLink = mmService.getPlayLink(assetId, mediafileDetails.getMediafileId(), "basiclti", "object"); /*TODO  change to current user, not generic */  
		} catch (ServiceException e) {
			embedLink = new LinkType();
			embedLink.setOutput("Error media cannot be played.");
			logger.error(e.getMessage(), e);
		}
		String embedcode = embedLink.getOutput();
		return embedcode;
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/play/{aId}/details")
	public String getPlayerDetails(@PathVariable("aId") String assetId,  ModelMap model) throws IOException {
	
		MediaMosaService mmService = mediamosaSpringService.getService();
		AssetDetailsType assetDetails = null;
		MediafileDetailsType mediafileDetails = null;
		LinkType embedLink = null;
		try {
			assetDetails = mmService.getAssetDetails(assetId);
			mediafileDetails = assetDetails.getMediafiles().getMediafile().get(0);
			embedLink = mmService.getPlayLink(assetId, mediafileDetails.getMediafileId(), "basiclti", "object"); /*TODO  change to current user, not generic */  
		} catch (ServiceException e) {
			embedLink = new LinkType();
			embedLink.setOutput("Error media cannot be played.");
			logger.error(e.getMessage(), e);
		}
		String embedcode = embedLink.getOutput();
		MediaBean media = new MediaBean();
		media.setAssetDetails(assetDetails);
		media.setMediafileDetails(mediafileDetails);
		media.setPlayer(embedcode);
		
		model.put("media", media);
		return "play";
	}
	
}
