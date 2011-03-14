<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<meta http-equiv="Content-type" content="text/html; charset=UTF-8" />
	<title>Gesci Test Page</title>
	<meta http-equiv="Content-Language" content="en-us" />
	<meta http-equiv="imagetoolbar" content="no" />
	<meta name="MSSmartTagsPreventParsing" content="true" />
	<meta name="description" content="Description" />
	<meta name="keywords" content="Keywords" />
	<meta name="author" content="Enlighten Designs" />
	<link type="text/css" href="css/global.css" media="screen" rel="stylesheet" />
	<link type="text/css" href="css/styles.css" media="screen" rel="stylesheet" />
	<style type="text/css" media="all">@import "../assets/templates/gesci/css/editor.css";</style>
	<style type="text/css" media="all">@import "../assets/templates/gesci/css/screen.css";</style>
	<style type="text/css" media="all">@import "../assets/templates/gesci/css/editor-backup.css";</style>
	<script type="text/javascript" src="js/swfobject.js"></script>
</head>
<body>
	<script type="text/javascript" src="js/JavaScriptFlashGateway.js"></script>
	<script type="text/javascript" src="js/Exception.js"></script>
	<script type="text/javascript" src="js/FlashTag.js"></script>
	<script type="text/javascript" src="js/FlashSerializer.js"></script>
	<script type="text/javascript" src="js/FlashProxy.js"></script>
	<div id='wrapper'>
		<div id="header">
			<div id="logo">
				<a href="index.html">
					<img src="../assets/templates/gesci/images/generic/gesci_logo.gif">
				</a>
			</div>
			<!-- flags code to insert translations -->
			<div id="flags">
				<a href="http://www.gesci.org"><img style="margin-right:6px;" src="../assets/templates/gesci/images/generic/uk_flag_down.gif"></a>
				<a href="http://www.gesci.org/fr"><img src="../assets/templates/gesci/images/generic/fr_flag_down.gif"></a>
			</div>
			<div id="searchbox">
				<form id="ajaxSearch_form" action="search-results.html" method="post">
					<fieldset>
					<input type="hidden" name="advSearch" value="oneword">
					<label for="ajaxSearch_input">
						<input id="ajaxSearch_input" class="cleardefault" type="text" name="search" value="Search here..." onfocus="this.value=(this.value=='Search here...')? '' : this.value ;">
					</label>
					<label for="ajaxSearch_submit">
						<input id="ajaxSearch_submit" type="submit" name="sub" value="Go!">
					</label>
					</fieldset>
				</form>
			</div>
			<div id="navigation">
				<div class="wrapper">
					<ul class="menu">
						<li class="category first current"><a href="http://www.gesci.org/" title="Home">Home</a></li>
						<li class="category "><a href="../about-us.html" title="About Us">About Us</a></li>
						<li class="category "><a href="../knowledge-society-for-all.html" title="Knowledge Society">Knowledge Society</a></li>
						<li class="category "><a href="../resources.html" title="Resources">Resources</a></li>
						<li class="category "><a href="../activities.html" title="Activities">Activities</a></li>
						<li class="category "><a href="../community.html" title="Community">Community</a></li>
						<li class="category last"><a href="../media.html" title="Media">Media</a></li>
					</ul>
				</div>
			</div>
		</div>
		<!--End of header-->
		<div id="main">
			<div id="inside">
				<h2>Knowledge Society Map</h2>
				<div id="map_container">
					<p style="margin-left:20px;"><a href="http://www.adobe.com/go/getflashplayer"><img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif" alt="Get Adobe Flash player" /></a></p>
					<?php
						$connection = mysql_connect("localhost", "u_richdel", "ahYeem6i");
						if(!$connection){
							die("Database connection failed: " . mysql_error());
						}
						$db_select = mysql_select_db("u_richdel", $connection);
						if(!db_select){
							die("Database selection failed: " . mysql_error());
						}
						$query = "SELECT name FROM country";
						$result = mysql_query($query, $connection);
						$body = "<div id='select_box'>";
						$body .= "<select>";
						while ($row = mysql_fetch_assoc($result)):
							$body.= sprintf("<option value=%s>" . $row["name"] . "</option>", $row["name"]);
						endwhile;
						$body .= "</select>";
						$body .= "</div>";
						
						echo $body;
					?>
				</div>
				<script type="text/javascript">
						var uid = new Date().getTime();
						var flashProxy = new FlashProxy(uid, 'js/JavaScriptFlashGateway.swf');<!--Makes zooming into countries by the selection box possible-->
						//var tag = new FlashTag('world.swf?data_file=country_new.xml', 900, 450);<!--Creating a flash tag from the XML file -->
						//tag.setFlashvars('lcId='+uid);
						//tag.write(document);
						swfobject.embedSWF("world.swf?data_file=country_new.xml", "map_container", "900", "500", "7.0.0", "expressInstall.swf", {lcId: uid}, {bgcolor: "#ffffff"});
				</script>
				
				<div class="help">
					<p>In the map click on the country you wish to see the knowledge society information.</p>
				</div>
				<?php
					$country = $_GET["country"];
					
					//if(isSet($country)){
						//echo "<br />You have selected <i>" . $country . "</i><br />";
					//}else{
						//$country = "";
					//}
					$connection = mysql_connect("localhost", "u_richdel", "ahYeem6i");//Last argument = password
					if(!$connection){
						die("Database connection failed: " . mysql_error());
					}
					
					$db_select = mysql_select_db("u_richdel", $connection);
					if(!$db_select){
						die("Database selection failed: " . mysql_error());
					}
					
					//Query for country ID
					$query = "SELECT * FROM country WHERE name LIKE '".$country."%'";
					$result = mysql_query($query, $connection);
					$row = mysql_fetch_array($result);
					$country_id = $row['id'];
					
					//Querying POLICIES
					$result = mysql_query("SELECT * FROM policies WHERE country_id = ".$country_id."", $connection);
					if(!$result){
						die("Database query failed: " . mysql_error());
					}
					$row = mysql_fetch_array($result);
					while($row){
					//echo "Website = " . $row['website'] . "<br />";
						$policies .= $row['website'] .  "<br/>";
						$row = mysql_fetch_array($result);
					}
					
					//Querying MINISTRY
					$result = mysql_query("SELECT * FROM ministry WHERE country_id = ".$country_id, $connection);
					if(!$result){
						die("Database query failed: " . mysql_error());
					}
					
					while($row = mysql_fetch_array($result)){
					//echo "Ministry = " . $row['website'] . "<br />";
						$ministry .= $row['website'] . "<br/>";
					}
					
					//Querying EDUCATION_PORTAL
					$result = mysql_query("SELECT * FROM education_portal WHERE country_id = ".$country_id, $connection);
					if(!$result){
						die("Database query failed: " . mysql_error());
					}
					
					while($row = mysql_fetch_array($result)){
					//echo "Education Portal = " . $row['website'] . "<br />";
							$education = $row['website'] . "<br/>";
					}
					
					//Querying CONTENT
					$result = mysql_query("SELECT * FROM content WHERE country_id = ".$country_id, $connection);
					if(!$result){
						die("Database query failed: " . mysql_error());
					}
					
					while($row = mysql_fetch_array($result)){
						$content = $row['comments'] . "<br/>";
					}
					
					//Closing database connection 
					mysql_close($connection);
					$body = "<div id='info'>";
					$body .= "<br /><table cellspacing='0'>";
					$body .= sprintf("<tr class='country_name'><td colspan=%s>%s</td></tr>", "2", $country); 
					
					$body .= sprintf("<tr class='country_ministry'><td>%s</td><td>%s</td></tr>", "<b>MoE Website</b>", $ministry); 
					$body .= sprintf("<tr class='country_education'><td>%s</td><td>%s</td></tr>", "<b>Educational Portal</b>", $education); 
					$body .= sprintf("<tr class='country_policies'><td>%s</td><td>%s</td></tr>", "<b>ICT/ICT4E Policies and Plans</b>", $policies); 
					$body .= sprintf("<tr class='country_content'><td>%s</td><td>%s</td></tr>", "<b>Policy Content / Notes</b>", $content); 
					
					$body .= "</table>";
					$body .= "</div>";
					
					echo $body;
				?>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
