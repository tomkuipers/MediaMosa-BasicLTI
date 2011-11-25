package nl.uva.mediamosa.basiclti.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.basiclti.web.model.UploadBean;
import nl.uva.mediamosa.model.UploadTicketType;
import nl.uva.mediamosa.util.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.imsglobal.basiclti.provider.api.BasicLtiContext;
import org.imsglobal.basiclti.provider.servlet.util.BasicLTIContextWebUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UploadController {

	protected static Logger logger = Logger.getLogger("UploadController");
	private static final String[] BASICLTI_ADMIN_ROLES = { "Instructor", "Administrator", "Mentor" };
	private boolean isInstructor = false;
	public String assetId;

	@Resource(name = "mediamosaSpringService")
	private MediaMosaSpringService mediamosaSpringService;

	// @ModelAttribute("upload")

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public ModelAndView getUploadForm(HttpSession session) throws ServiceException, IOException {

		MediaMosaService mmService = mediamosaSpringService.getService();
		assetId = mmService.createAsset("basiclti"); //basiclti /* TODO use current user */
		String mediafileId = mmService.createMediafile(assetId, "basiclti"); //basiclti /* TODO use current user */
		UploadTicketType uploadTicket = mmService.createUploadTicket(mediafileId,	"basiclti"); //basiclti /* TODO use current user */
		String uploadUrl = uploadTicket.getAction();

		UploadBean upload = new UploadBean();
		upload.setOwner("basiclti"); //basiclti /* TODO use current user */
		upload.setAssetId(assetId);
		
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		List<String> bltiroles = bltictx.getUser().getRoles();
		this.isInstructor = false;
		for (String role : BASICLTI_ADMIN_ROLES) {
	    	for (String bltirole : bltiroles) {
	        	if (StringUtils.containsIgnoreCase(bltirole, role)) {
	        		this.isInstructor = true;
	        	}        		
	    	}
	    }

		ModelAndView mav = new ModelAndView();
		mav.setViewName("upload");
		mav.addObject("isInstructor", this.isInstructor);
		
		mav.addObject("imageUrl", "images/XPButtonUploadText_61x22.png");
		mav.addObject("flashUrl", "swf/swfupload.swf");
		mav.addObject("uploadUrl", uploadUrl);
		mav.addObject("filetypes", "*.flv;*.f4v;*.mp4;*.mov;*.wmv;*.asf;*.mp3;");
		return mav;

	}

	@RequestMapping(value = "/upload2", method = RequestMethod.POST)
	public String processUploadForm(@ModelAttribute UploadBean upload, ModelMap model) throws IOException {
		
		String assetId  = upload.getAssetId();
  		String title = upload.getTitle();
  		String description = upload.getDescription();
  		String userName = upload.getOwner();
  		
  		Map <String, String> metadata = new HashMap <String, String>();
  		metadata.put("title", title);
  		metadata.put("description", description);
  		
  		MediaMosaService mmService = mediamosaSpringService.getService();
  		try {
			mmService.setMetadata(assetId, userName, metadata);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
		}
  		
		return "tool";
	}

}
