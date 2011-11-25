package nl.uva.mediamosa.basiclti.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nl.uva.mediamosa.MediaMosaService;
import nl.uva.mediamosa.SearchParameters;
import nl.uva.mediamosa.basiclti.service.BasicLTIResourceService;
import nl.uva.mediamosa.basiclti.web.model.BasicLTIResource;
import nl.uva.mediamosa.basiclti.web.model.ToolBean;
import nl.uva.mediamosa.basiclti.web.model.UploadBean;
import nl.uva.mediamosa.basiclti.web.model.SearchBean;
import nl.uva.mediamosa.model.AssetType;
import nl.uva.mediamosa.model.UploadTicketType;
import nl.uva.mediamosa.util.ServiceException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.imsglobal.basiclti.provider.api.BasicLtiContext;
import org.imsglobal.basiclti.provider.servlet.util.BasicLTIContextWebUtil;
import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class ToolController {
	
	protected static final Logger logger = Logger.getLogger("ToolController");
	// @see http://www.imsglobal.org/lti/blti/bltiv1p0/ltiBLTIimgv1p0.html
	private static final String[] BASICLTI_ADMIN_ROLES = { "Instructor", "Administrator", "Mentor" };
	
	@Resource(name="mediamosaSpringService")
	private MediaMosaSpringService mediamosaSpringService;
	
	@Resource(name="btliResourceService")
	private BasicLTIResourceService bltiResourceService; 
	
	/*
	 * The launch from a Basic LTI consumer is a form POST.
	 * This is the main entry to the Basic LTI provider application
	 */
	@RequestMapping(value = "/tool", method = RequestMethod.POST)
	public ModelAndView showTool(HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		String bltilocale = bltictx.getLaunchPresentation().getLocale();
				
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		if (localeResolver != null && bltilocale != null) {
			LocaleEditor localeEditor = new LocaleEditor();
			// fix for locale containing hyphen (-) instead of underscore (_)
			String locale = bltilocale.replaceAll("-", "_");		
			localeEditor.setAsText(locale);
			// set Locale
			localeResolver.setLocale(request, null, (Locale) localeEditor.getValue());
		}
		
		ToolBean tool = populate(bltictx);

		MediaMosaService mmService = mediamosaSpringService.getService();
		List<AssetType> assetList = null;
		try {
			assetList = mmService.getAssets();
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("tool");
		mav.addObject("tool", tool);
		mav.addObject("assets", assetList);
		
		/*hit db*/
		String resourceLinkId = bltictx.getResourceLink().getId();
		String contextId = bltictx.getContext().getId();
		List<BasicLTIResource> bltiResources = bltiResourceService.getAllForContext(resourceLinkId, contextId);

		mav.addObject("resources" , bltiResources);
		mav.addObject("basiclticontext", bltictx);
		mav.addObject("basiclticontext_resourcelink_id", resourceLinkId);
		mav.addObject("basiclticontext_context_id", contextId);
		
		return mav;
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/tool/add/{aId}")
	public String addAssetToTool(@PathVariable("aId") String assetId, HttpSession session, ModelMap model) {
		
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		ToolBean tool = populate(bltictx);
		
		String resourceLinkId = bltictx.getResourceLink().getId();
		String contextId = bltictx.getContext().getId();
		String title = "";
		String description = "";
		bltiResourceService.add(assetId, resourceLinkId, contextId, title, description);
		List<BasicLTIResource> bltiResources = bltiResourceService.getAllForContext(resourceLinkId, contextId);
		model.put("resources", bltiResources);
		model.put("tool", tool);
		return "tool";
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/tool/delete/{id}")
	public String removeAssetFromTool(@PathVariable("id") int id, HttpSession session, ModelMap model) {
		
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		ToolBean tool = populate(bltictx);

		String resourceLinkId = bltictx.getResourceLink().getId();
		String contextId = bltictx.getContext().getId();
		bltiResourceService.delete(id);
		List<BasicLTIResource> bltiResources = bltiResourceService.getAllForContext(resourceLinkId, contextId);

		model.put("resources", bltiResources);
		model.put("tool", tool);
		return "tool";
	}

	@RequestMapping(value = "/tool/edit/{id}", method = RequestMethod.GET)
	public String editToolResource(@PathVariable("id") int id, HttpSession session, ModelMap model) {
		
		BasicLTIResource resource = bltiResourceService.getResource(id).get(0);
		model.addAttribute("resource", resource);
		return "edit";
	}
	
	@RequestMapping(value = "/tool/edit", method = RequestMethod.POST)
	public String prccessEditForm(@ModelAttribute("resource") BasicLTIResource resource, BindingResult result, ModelMap model) {
		
		Integer id = resource.getId();
		String assetId = resource.getAssetId();
		String resourceLinkId = resource.getResourceLinkId();
		String contextId = resource.getContextId();
		String title = resource.getTitle();
		String description = resource.getDescription();
		
		bltiResourceService.edit(id, assetId, resourceLinkId, contextId, title, description);
		
		return "closewindow";
	}
	
	/*
	 * If you intend to handle the same path in multiple methods, then factor them out into a dedicated handler class with that path mapped at the type level!
	 */
	
	@RequestMapping(value= "/tool", method = RequestMethod.GET)
	public String getToolResources(HttpSession session, ModelMap model) {

		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		ToolBean tool = populate(bltictx);
		
		/*hit db*/
		List<BasicLTIResource> bltiResources = bltiResourceService.getAllForContext(bltictx.getResourceLink().getId(), bltictx.getContext().getId());
		model.put("resources" , bltiResources);
		model.put("basiclticontext_resourcelink_id", bltictx.getResourceLink().getId());
		model.put("tool", tool);
		
		return "tool";
	}
	
	@RequestMapping(value="/tool/browse", method=RequestMethod.GET)
	public String browseAssets(HttpSession session, ModelMap model) {
		
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		ToolBean tool = populate(bltictx);
		
		MediaMosaService mmService = mediamosaSpringService.getService();
		
		Map <String, String> searchParams = new HashMap <String, String>();
		searchParams.put(SearchParameters.OWNERID, tool.getMmUsername());		
		List<AssetType> assetList = null;
		try {
			assetList = mmService.getAssets(searchParams);
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		model.put("assets", assetList);
		model.put("tool", tool);
		return "browse";
	}
	
	@RequestMapping(value = "/tool/upload", method = RequestMethod.POST)
	public String processUploadForm(@ModelAttribute("upload") UploadBean upload, BindingResult result, ModelMap model) throws IOException {
		
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
  		
		return "redirect:/mmblti/tool";
	}
	
	@RequestMapping(value="/tool/upload", method = RequestMethod.GET)
	public String initUploadForm(HttpSession session, ModelMap model) throws ServiceException, IOException {
		
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		ToolBean tool = populate(bltictx);
		
		MediaMosaService mmService = mediamosaSpringService.getService();
		String assetId = mmService.createAsset(tool.getMmUsername());
		String mediafileId = mmService.createMediafile(assetId, tool.getMmUsername());
		UploadTicketType uploadTicket = mmService.createUploadTicket(mediafileId, tool.getMmUsername());
		String uploadUrl = uploadTicket.getAction();

		UploadBean upload = new UploadBean();
		upload.setOwner(tool.getMmUsername());
		upload.setAssetId(assetId);
		upload.setImageurl("/mmbltiprovider/images/XPButtonUploadText_61x22.png");
		upload.setFlashurl("/mmbltiprovider/swf/swfupload.swf");
		upload.setUploadurl(uploadUrl);
		upload.setFiletypes("*.flv;*.f4v;*.m4v;*.mp4;*.mov;*.wmv;*.asf;*.mp3;");
		
		model.addAttribute("upload", upload);
		model.addAttribute("tool", tool);
		
		return "upload";
	}
	
	@RequestMapping(value="/tool/search", method=RequestMethod.GET)
	public String initSearchForm(HttpSession session, ModelMap model) {
					
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		ToolBean tool = populate(bltictx);

		SearchBean search = new SearchBean();
		List<AssetType> assetList = null;
		
		model.addAttribute("search", search);
		model.addAttribute("assets", assetList);
		model.addAttribute("tool", tool);
		return "search";
	}
	
	@RequestMapping(value="/tool/search", method=RequestMethod.POST)
	public String processSearchForm(@ModelAttribute("search") SearchBean search, BindingResult result, ModelMap model, HttpSession session) throws IOException {

		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		ToolBean tool = populate(bltictx);
		
  		String searchterm  = search.getSearchTerm();
  		String cql = "((title all \"" + searchterm +  "\") OR (description all \"" + searchterm + "\") OR (subject all \"" + searchterm + "\") OR (owner_id all \"" + searchterm + "\") OR (group_id all \"" + searchterm + "\"))";
  		List<AssetType> assetList = null;
  		
  		// only perform search when keyword is provided
  		if (!StringUtils.isEmpty(searchterm)) {	
  			MediaMosaService mmService = mediamosaSpringService.getService();
  			try {
  				assetList = mmService.getCqlAssets(cql);
  			} catch (ServiceException e) {
  				logger.error(e.getMessage());
  			}
  		}
		
  		model.addAttribute("search", search);
  		model.addAttribute("assets", assetList);
  		model.addAttribute("tool", tool);
		return "search";
	}

	@RequestMapping(value="/tool/logout", method=RequestMethod.GET)
	public String logout(HttpSession session, ModelMap model) {
		
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		String returnUrl = bltictx.getLaunchPresentation().getReturnUrl();
		String logoutRedirect = "redirect:/";
		if(returnUrl != null) {
			logoutRedirect = "redirect:" + returnUrl;
		}
		session.invalidate();
		
		return logoutRedirect;
	}
	
	@RequestMapping(value="/tool/asset/{assetId}/delete", method=RequestMethod.POST)
	public String deleteAsset(@PathVariable("assetId") String assetId, HttpSession session, ModelMap model) throws IOException {
	
		BasicLtiContext bltictx = BasicLTIContextWebUtil.getBasicLtiContext(session);
		ToolBean tool = populate(bltictx);
				
		MediaMosaService mmService = mediamosaSpringService.getService();
		
		try {
			mmService.deleteAsset(assetId, tool.getMmUsername(), true);
		} catch (ServiceException e) {
			logger.error(e.getMessage());
		}
		
		model.addAttribute("tool", tool);
		return "browse";
	}
	
	private ToolBean populate(BasicLtiContext bltictx) {
		ToolBean tool = new ToolBean();
		
		String fullname = bltictx.getLisPerson().getNameFull();
		if(!StringUtils.isEmpty(fullname)) {
			tool.setInstructorName(fullname);
		}
		
		String contexTitle = bltictx.getContext().getTitle();
		if(!StringUtils.isEmpty(contexTitle)) {
			tool.setContextTitle(contexTitle);
		}
		
		List<String> bltiroles = bltictx.getUser().getRoles();
		for (String role : BASICLTI_ADMIN_ROLES) {
	    	for (String bltirole : bltiroles) {
	        	if (StringUtils.containsIgnoreCase(bltirole, role)) {
	        		tool.setInstructor(true);
	        	}      		
	    	}
	    }
		
	
		String sourcedid = bltictx.getLisPerson().getSourcedId(); 
		if (!StringUtils.isEmpty(sourcedid)) {
			tool.setMmUsername(sourcedid);	
		} else {
			String userid = bltictx.getUser().getId();
			if (!StringUtils.isEmpty(userid)) {
				tool.setMmUsername(userid);
			} else {
				// store ext_lms??
				tool.setMmUsername("basiclti");	
			}
		}

		return tool;
	}
}
