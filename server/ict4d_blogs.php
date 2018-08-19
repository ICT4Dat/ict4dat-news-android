<?php

//get the last-modified-date of this very file
$lastModified=filemtime(__FILE__);
//get a unique hash of this file (etag)
$etagFile = md5_file(__FILE__);
//get the HTTP_IF_MODIFIED_SINCE header if set
$ifModifiedSince=(isset($_SERVER['HTTP_IF_MODIFIED_SINCE']) ? $_SERVER['HTTP_IF_MODIFIED_SINCE'] : false);
//get the HTTP_IF_NONE_MATCH header if set (etag: unique file hash)
$etagHeader=(isset($_SERVER['HTTP_IF_NONE_MATCH']) ? trim($_SERVER['HTTP_IF_NONE_MATCH']) : false);

//set last-modified header
header("Last-Modified: ".gmdate("D, d M Y H:i:s", $lastModified)." GMT");
//set etag-header
header("Etag: $etagFile");
//make sure caching is turned on
header('Cache-Control: public');

//check if page has changed. If not, send 304 and exit
if (@strtotime($_SERVER['HTTP_IF_MODIFIED_SINCE'])==$lastModified || $etagHeader == $etagFile) {
	header("HTTP/1.1 304 Not Modified");
	exit;
}


header('Content-type:application/json;charset=utf-8');

// The URL will be used as a primary key, careful when changing it!

// 0 -> Self Hosted Wordpress Blog
// 1 -> Wordpress.com
// 2 -> RSS

$array = array(
	array("name" => "ICT4D.at",  
		"description" => "Austrian Network for Information and Comunication Technologies for Development", 
		"url" => "http://www.ict4d.at/",
		"feed_type" => 0,
		"logo_url" => "http://www.ict4d.at/wp-content/themes/ict4d.at/images/header_logo.png"),

	array("name" => "Tim Unwin's Blog",  
		"description" => "Blog from Tim Unwin, UNESCO Chair in ICT4D and Emeritus Professor of Geography at Royal Holloway, University of London.", 
		"url" => "https://unwin.wordpress.com/feed/",
		"feed_type" => 1,
		"logo_url" => "https://unwin.files.wordpress.com/2017/09/tim-small-wsis.jpg"),

	array("name" => "CIPESA",  
		"description" => "Promoting Effective and Inclusive ICT Policy in Africa.", 
		"url" => "https://cipesa.org/",
		"feed_type" => 0,
		"logo_url" => "https://cipesa.org/wp-content/uploads/2016/05/cipesa-logo-small.png"),

	array("name" => "UNICEF Stories",  
		"description" => "Stories and Innovation", 
		"url" => "http://unicefstories.org/",
		"feed_type" => 0,
		"logo_url" => "http://s21028.pcdn.co/wp-content/uploads/2018/04/Artboard-3.png"),

	array("name" => "IT for Change",  
		"description" => "Bridging Development Realities and Technological Possibilities", 
		"url" => "https://www.itforchange.net/rss.xml",
		"feed_type" => 2,
		"logo_url" => "http://www.itforchange.net/sites/default/files/high-res-itfc-logo1_4.png"),

	array("name" => "Linnet Taylor",  
		"description" => "doubt wisely, and never lose your penguin.", 
		"url" => "https://linnettaylor.wordpress.com/feed/",
		"feed_type" => 1,
		"logo_url" => "null"),

	array("name" => "Open Knowledge International Blog",  
		"description" => "Open Knowledge International is a global non-profit organisation focused on realising open data’s value to society by helping civil society groups access and use data to take action on social problems.", 
		"url" => "http://blog.okfn.org/feed/",
		"feed_type" => 1,
		"logo_url" => "https://i1.wp.com/blog.okfn.org/files/2017/08/Logo-OKI-landscape-rgb-936x244.png?fit=600%2C156"),

	array("name" => "iRevolutions",  
		"description" => "Hacking emerging technologies to solve emerging aid & development problems.", 
		"url" => "https://irevolutions.org/feed/",
		"feed_type" => 1,
		"logo_url" => "https://irevolution.files.wordpress.com/2008/04/cropped-oath.jpg"),

	array("name" => "My heart's in Accra",  
		"description" => "Personal blog from Ethan Zuckerman, director of the Center for Civic Media at MIT.", 
		"url" => "http://www.ethanzuckerman.com/blog/",
		"feed_type" => 0,
		"logo_url" => "http://www.ethanzuckerman.com/blog/wp-content/2013/05/cropped-bernardheader1.jpg"),

	array("name" => "Appropriating Technology",  
		"description" => "Blog from Tony Roberts, fellow in the Digital & Technology team at the Institute for Development Studies at the University of Sussex in the UK.", 
		"url" => "http://appropriatingtechnology.org/?q=rss.xml",
		"feed_type" => 2,
		"logo_url" => "http://appropriatingtechnology.org/sites/default/files/asikana%20video%20banner%202.jpg"),

	array("name" => " Chris Blattman",  
		"description" => "Blog from Chris Blattman, development economist at the Harris School of Public Policy at the University of Chicago. Focus is on development, but he's young and hip and offers great perspective on local technology interventions.", 
		"url" => "https://chrisblattman.com/feed",
		"feed_type" => 1,
		"logo_url" => "https://chrisblattman.com/wp-content/themes/nimble/images/blattman-s.png"),

	array("name" => "Digital@DAI",  
		"description" => "A relatively new but well-written blog covering recent technologies, experiments with tech solutions, and reviews.", 
		"url" => "https://dai-global-digital.com/feed.xml",
		"feed_type" => 2,
		"logo_url" => "https://dai-global-digital.com/uploads/social-image.jpg"),

	array("name" => "EduTech",  
		"description" => "One of the best blogs on ICT, with a focus on education, from Mike Trucano of the World Bank.", 
		"url" => "https://blogs.worldbank.org/edutech/rss.xml",
		"feed_type" => 2,
		"logo_url" => "https://blogs.worldbank.org/edutech/files/edutech/edutech-banner-940-updated.png"),


	array("name" => "GSMA Newsroom",  
		"description" => "Blog for the trade union of 800 mobile operators and 300 companies around the world.", 
		"url" => "https://www.gsma.com/newsroom/category/press-release/feed/",
		"feed_type" => 1,
		"logo_url" => "https://www.gsma.com/wp-content/plugins/plugin_header_overload/images/logo.png"),


	array("name" => "Guardian Project Blog",  
		"description" => "Blog for the non-profit group creating apps and promoting libraries and devices that protect data and communications.", 
		"url" => "https://guardianproject.info/",
		"feed_type" => 0,
		"logo_url" => "null"),

	array("name" => "ICT Ethics",  
		"description" => "Analyzing, clustering and summarizing the ethical debates in ICTD", 
		"url" => "https://ictdethics.wordpress.com/feed/",
		"feed_type" => 1,
		"logo_url" => "null"),

	array("name" => "ICT4D Views from the Field",  
		"description" => "Blog about ICT4D projects from Dr. Laura Hosman, at the School for the Future of Innovation in Society and at The Polytechnic School at Arizona State University.", 
		"url" => "https://ict4dviewsfromthefield.wordpress.com/feed/",
		"feed_type" => 1,
		"logo_url" => "https://ict4dviewsfromthefield.files.wordpress.com/2013/04/cropped-p1120598-copy.jpg"),

	array("name" => "ICT4Peace Blog",  
		"description" => "Conflict resolution and ICT4D blog.", 
		"url" => "https://ict4peace.wordpress.com/feed/",
		"feed_type" => 1,
		"logo_url" => "https://defaultcustomheadersdata.files.wordpress.com/2016/07/design2.jpg?resize=1000,200"),

	array("name" => "ICTLogy Blog",  
		"description" => "From Ismael Peña-López, one of the longest-running ICT sites and blogs. Many great links and resources.", 
		"url" => "http://ictlogy.net/",
		"feed_type" => 0,
		"logo_url" => "http://ictlogy.net/common/img/ismael1.jpg"),

	array("name" => "ICTs for Development",  
		"description" => "Blog associated with [University of Manchester’s Centre for Development Informatics Blog associated with University of Manchester’s Centre for Development Informatics. Richard Heeks is a must-follow.", 
		"url" => "https://ict4dblog.wordpress.com/feed/",
		"feed_type" => 1,
		"logo_url" => "null"),

	array("name" => "ICTWorks",  
		"description" => "ICTworks is the premier community for international development professionals committed to utilizing new and emerging technologies to magnify the intent of communities to accelerate their social and economic development.", 
		"url" => "https://www.ictworks.org/",
		"feed_type" => 0,
		"logo_url" => "https://www.ictworks.org/ictworkslogo.png"),

	array("name" => "kiwanja.net",  
		"description" => "Blog from Ken Banks, one of ICT4D's most recognized practitioners.", 
		"url" => "http://www.kiwanja.net/",
		"feed_type" => 0,
		"logo_url" => "http://www.kiwanja.net/wp-content/uploads/2016/04/rotated-kiwanja-logo.jpg"),

	array("name" => "Many Possibilities",  
		"description" => "My name is Steve Song.  I am the Founder of Village Telco, a social enterprise that builds low-cost WiFi mesh VoIP technologies to deliver affordable voice and Internet in underserviced areas.", 
		"url" => "https://manypossibilities.net/",
		"feed_type" => 0,
		"logo_url" => "https://i0.wp.com/manypossibilities.net/wp-content/uploads/2008/01/SteveSong-003_800px-300x199.jpg?resize=300%2C199"),

	array("name" => "Matt Haikin Blog",  
		"description" => "Matt Haikin – ICT4D practitioner, researcher and consultant. Exploring participatory approaches to ICT, and technologies to enable participation.", 
		"url" => "https://matthaikin.com/feed/",
		"feed_type" => 1,
		"logo_url" => "null"),

	array("name" => "Mobile for Development (GSMA) Blog",  
		"description" => "Blog for the team within GSMA that focuses on emerging markets.", 
		"url" => "https://www.gsma.com/mobilefordevelopment/feed/",
		"feed_type" => 0,
		"logo_url" => "https://www.gsma.com/mobilefordevelopment/wp-content/plugins/plugin_header_overload/images/logo.png")
	
	);

echo json_encode($array);
?>
