MediaMosa BasicLTI tool provider
   
This software offers an IMS BasicLTI provider tool for streaming media functionality provided by a MediaMosa platform. 
More information about the BasicLTI standard can be found on http://www.imsglobal.org/developers/BLTI/materials/2010-06-06-Basic-LTI-Primer.pdf


Configuring BasicLTI consumers

How do you configure Sakai 2.8?
- Create a new account (http://qa1-nl.sakaiproject.org/portal/site/!gateway/page/!gateway-700) - You are now logged in.
- Left handside tool list - Worksite Setup
- New | Edit | Delete (Click on new)
- Project site
- Site Title (choose a unique title)
- Continue
- Project site Tools (check basic LTI)
- Continue x 3
- Create Site
- Click on site tab
- Basic LTI tool link
- Right handside (icon of paper and pencil) - Click
- Remote tool url - http://your_host:8080/mmbltiprovider/mmblti (replace your_host with your host where you are running the provider)
- Remote tool key - Sakai_consumer
- Remote tool Secret - (secret)
- update options

How do you configure Blackboard 9.1 SP4 (and later)?
- As a System Admin configure Blackboard's Basic LTI Tools Providers (http://library.blackboard.com/ref/df5b20ed-ce8d-4428-a595-a0091b23dda3/Content/_admin_app_system/admin_app_basic_lti_tool_providers.htm). 
  Go to System Admin > Building Blocks > Basic LTI Tools Providers > Register Provider Domain
- Provider Domain: your_host (omit protocol and portnumbers)
- Provider Domain Status: Approved
- Default Configuration: Set globally
- Tool Provider Key: Blackboard_consumer
- Tool Provider Secret: (secret)
- Send User Data: Send user data over any connection
- User Fields to Send: (check Roles in Course, Name and Email Address)
- Show User Acknowledgment Message: No
- Submit
- As a System Admin go to System Admin > Course Settings > Course Tools to enable Basic LTI as a course tool.
- As an Instructor create a link in a course. Go to Content Area > Build Content > URL
- Name: (some description)
- Url: http://your_host:8080/mmbltiprovider/mmblti (replace your_host with your host where you are running the provider)
- Check 'This is a link to a Tool Provider'
- Submit