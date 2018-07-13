<?php
header('Content-type:application/json;charset=utf-8');
$array = array(array("name" => "My heart's in Accra",  
              "description" => "Personal blog from Ethan Zuckerman, director of the Center for Civic Media at MIT.", 
              "url" => "http://www.ethanzuckerman.com/blog/feed/",
              "feed_type" => "xml",
			  "logo_url" => "http://www.ethanzuckerman.com/blog/wp-content/2013/05/cropped-bernardheader1.jpg"),
			  
			  array("name" => "Appropriating Technology",  
              "description" => "Blog from Tony Roberts, fellow in the Digital & Technology team at the Institute for Development Studies at the University of Sussex in the UK.", 
              "url" => "http://appropriatingtechnology.org/?q=rss.xml",
              "feed_type" => "xml",
			  "logo_url" => "http://appropriatingtechnology.org/sites/default/files/asikana%20video%20banner%202.jpg"),
             
			array("name" => " Chris Blattman",  
              "description" => "Blog from Chris Blattman, development economist at the Harris School of Public Policy at the University of Chicago. Focus is on development, but he's young and hip and offers great perspective on local technology interventions.", 
              "url" => "https://chrisblattman.com/feed",
              "feed_type" => "xml",
			  "logo_url" => "https://chrisblattman.com/wp-content/themes/nimble/images/blattman-s.png"),
			  
			    array("name" => "Development Gateway Blog",  
              "description" => "Blog for the NGO focused on combining data, technology, and people. Good examples of tools built for actual use by/in organizations.", 
              "url" => "No rss link found",
              "feed_type" => "null",
			  "logo_url" => "null"),
			  
			    array("name" => "Digital@DAI",  
              "description" => "A relatively new but well-written blog covering recent technologies, experiments with tech solutions, and reviews.", 
              "url" => "https://dai-global-digital.com/feed.xml",
              "feed_type" => "xml",
			  "logo_url" => "https://dai-global-digital.com/uploads/social-image.jpg"),
			  
			  array("name" => "EduTech",  
              "description" => "One of the best blogs on ICT, with a focus on education, from Mike Trucano of the World Bank.", 
              "url" => "https://blogs.worldbank.org/edutech/rss.xml",
              "feed_type" => "xml",
			  "logo_url" => "https://blogs.worldbank.org/edutech/files/edutech/edutech-banner-940-updated.png"),
			  
			  
			  array("name" => "GSMA Newsroom",  
              "description" => "Blog for the trade union of 800 mobile operators and 300 companies around the world.", 
              "url" => "https://www.gsma.com/newsroom/category/press-release/feed/",
              "feed_type" => "xml",
			  "logo_url" => "https://www.gsma.com/wp-content/plugins/plugin_header_overload/images/logo.png"),
			  
			  
			  array("name" => "Guardian Project Blog",  
              "description" => "Blog for the non-profit group creating apps and promoting libraries and devices that protect data and communications.", 
              "url" => "https://guardianproject.info/blog/feed/",
              "feed_type" => "xml",
			  "logo_url" => "null"),
			  
			  array("name" => "ICT Ethics",  
              "description" => "Analyzing, clustering and summarizing the ethical debates in ICTD", 
              "url" => "https://ictdethics.wordpress.com/feed/",
              "feed_type" => "xml",
			  "logo_url" => "null"),
			  
			  array("name" => "ICT4D Views from the Field",  
              "description" => "Blog about ICT4D projects from Dr. Laura Hosman, at the School for the Future of Innovation in Society and at The Polytechnic School at Arizona State University.", 
              "url" => "https://ict4dviewsfromthefield.wordpress.com/feed/",
              "feed_type" => "xml",
			  "logo_url" => "https://ict4dviewsfromthefield.files.wordpress.com/2013/04/cropped-p1120598-copy.jpg"),
			  
			  array("name" => "ICT4D.at",  
              "description" => "Austrian blog network for ICT4D professionals.", 
              "url" => "http://www.ict4d.at/blog/feed/",
              "feed_type" => "xml",
			  "logo_url" => "http://www.ict4d.at/wp-content/themes/ict4d.at/images/header_logo.png"),
			  
			  array("name" => "ICT4Peace Blog",  
              "description" => "Conflict resolution and ICT4D blog.", 
              "url" => "https://ict4peace.wordpress.com/feed/",
              "feed_type" => "xml",
			  "logo_url" => "https://defaultcustomheadersdata.files.wordpress.com/2016/07/design2.jpg?resize=1000,200"),
			  
			  array("name" => "ICTLogy Blog",  
              "description" => "From Ismael Peña-López, one of the longest-running ICT sites and blogs. Many great links and resources.", 
              "url" => "http://ictlogy.net/feed/",
              "feed_type" => "xml",
			  "logo_url" => "http://ictlogy.net/common/img/ismael1.jpg"),
			  
			  array("name" => "ICTs for Development",  
              "description" => "Blog associated with [University of Manchester’s Centre for Development Informatics Blog associated with University of Manchester’s Centre for Development Informatics. Richard Heeks is a must-follow.", 
              "url" => "https://ict4dblog.wordpress.com/feed/",
              "feed_type" => "xml",
			  "logo_url" => "null"),
			  
			  array("name" => "ICTWorks",  
              "description" => "One of the most well-known blogs for all things ICT.", 
              "url" => "https://www.ictworks.org/feed/",
              "feed_type" => "xml",
			  "logo_url" => "https://www.ictworks.org/ictworkslogo.png"),
			  
			  array("name" => "ITU Newslog",  
              "description" => "The International Telecommunications Union (ITU) blog — they have some of the best data and global reports.", 
              "url" => "null",
              "feed_type" => "null",
			  "logo_url" => "null"),
			  
			  array("name" => "kiwanja.net",  
              "description" => "Blog from Ken Banks, one of ICT4D's most recognized practitioners.", 
              "url" => "http://www.kiwanja.net/feed/",
              "feed_type" => "xml",
			  "logo_url" => "http://www.kiwanja.net/wp-content/uploads/2016/04/rotated-kiwanja-logo.jpg"),
			  
			  array("name" => "Knowledge Management for Development Blog",  
              "description" => "Fairly good and wide-ranging blog with new articles often (despite the dated appearance of the site).", 
              "url" => "http://www.km4dev.org/activity/log/list?fmt=rss",
              "feed_type" => "xml",
			  "logo_url" => "null"),
			  
			  array("name" => "Many Possibilities",  
              "description" => "Blog from ICT4D giant Steve Song.", 
              "url" => "https://manypossibilities.net/feed/",
              "feed_type" => "xml",
			  "logo_url" => "null"),
			  
			   array("name" => "Matt Haikin Blog",  
              "description" => "Personal site/blog of ICT4D professional in the UK. Great links to Tech for M&E and ICT4D Meetups.", 
              "url" => "https://matthaikin.com/feed/",
              "feed_type" => "xml",
			  "logo_url" => "null"),
			  
			  array("name" => "Mobile for Development (GSMA) Blog",  
              "description" => "Blog for the team within GSMA that focuses on emerging markets.", 
              "url" => "https://www.gsma.com/mobilefordevelopment/feed/",
              "feed_type" => "xml",
			  "logo_url" => "https://www.gsma.com/mobilefordevelopment/wp-content/plugins/plugin_header_overload/images/logo.png"),
			  
			   array("name" => "Panoply Digital Blog",  
              "description" => "Blog of the consultancy organization in the UK that focuses on ICT.", 
              "url" => "null",
              "feed_type" => "null",
			  "logo_url" => "https://static1.squarespace.com/static/56934caa57eb8d9880b973fe/t/5abe4202f950b74a7145f3f5/1528827409384/?format=1000w"),
			  
			  array("name" => "Tim Unwin's Blog",  
              "description" => "Blog from Tim Unwin, UNESCO Chair in ICT4D and Emeritus Professor of Geography at Royal Holloway, University of London.", 
              "url" => "https://unwin.wordpress.com/feed/",
              "feed_type" => "xml",
			  "logo_url" => "null"),
			  
			  array("name" => "Timbuktu Chronicles",  
              "description" => "Blog from Tim Unwin, UNESCO Chair in ICT4D and Emeritus Professor of Geography at Royal Holloway, University of London.", 
              "url" => "https://unwin.wordpress.com/feed/",
              "feed_type" => "xml",
			  "logo_url" => "null"));
			 
echo json_encode($array);
?>
