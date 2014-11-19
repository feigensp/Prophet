<?php
	require_once('config.php');
	require_once('constants.php');	
	
	header("Content-Type: text/html; charset=UTF-8");

	function myDie($currNode, $error) {
		$experiment = $currNode[0];
		if ($experiment !=0) {
			mysql_query("DELETE FROM experiments WHERE experiment=\"$experiment\"");
			mysql_query("DELETE FROM nodes WHERE experiment=\"$experiment\"");
			mysql_query("DELETE FROM nodes_attributes WHERE experiment=\"$experiment\"");
		}
		die($error);
	}
	function loadNode($node, $currNode, $level = 0) {
		//check for consistency
		if ($level==0) {
			$expected=NODE_EXPERIMENT;
		}
		if ($level==1) {
			$expected=NODE_CATEGORY;
		}
		if ($level==2) {
			$expected=NODE_QUESTION;
		}
		if ($level<0 || $level>2) {
			myDie($currNode,"Wrong experiment XML format.\nLevel: ".$level);
		}		
		if ($node->getName() !== $expected) {
			myDie($currNode,"Wrong experiment XML format.\nExpected: ".$expected."\nFound: ".$node->getName());
		}
		
		//node
		$name = mysql_real_escape_string($node[KEY_NAME]);
		$value = mysql_real_escape_string($node[KEY_VALUE]);
		mysql_query("INSERT INTO nodes (experiment, category, question, name, value) VALUES ('".$currNode[0]."', '".$currNode[1]."', '".$currNode[2]."', '$name', '$value')")
			or myDie($currNode,'Cannot insert node: ' . mysql_error());
		
		//attributes
		if ($node->attributes[0]->attribute) {
			foreach($node->attributes[0]->attribute as $attribute) {
				switch((string)$attribute[KEY_NAME]) {
					case NAME_DONOTSHOWCONTENT:
						if (!strcmp($attribute[KEY_VALUE],VALUE_TRUE)) {
							$attributes[] = "('".$currNode[0]."', '".$currNode[1]."', '".$currNode[2]."', '".NAME_DONOTSHOWCONTENT."', '1')";
						} else if (!strcmp($attribute[KEY_VALUE],VALUE_FALSE)) {
							$attributes[] = "('".$currNode[0]."', '".$currNode[1]."', '".$currNode[2]."', '".NAME_DONOTSHOWCONTENT."', '0')";
						}
						break;
					case NAME_INACTIVE:
						if (!strcmp($attribute[KEY_VALUE],VALUE_TRUE)) {
							$attributes[] = "('".$currNode[0]."', '".$currNode[1]."', '".$currNode[2]."', '".NAME_INACTIVE."', '1')";
						} else if (!strcmp($attribute[KEY_VALUE],VALUE_FALSE)) {
							$attributes[] = "('".$currNode[0]."', '".$currNode[1]."', '".$currNode[2]."', '".NAME_INACTIVE."', '0')";
						}
						break;
					case NAME_NOTES:
						$attributes[] = "('".$currNode[0]."', '".$currNode[1]."', '".$currNode[2]."', '".NAME_NOTES."', '".mysql_real_escape_string($attribute[KEY_VALUE])."')";
						break;
				}
			}
		}
		if ($attributes) {
			$request = "INSERT INTO nodes_attributes (experiment, category, question, name, value) VALUES ".$attributes[0];
			$i = 1;
			while ($attributes[$i]) {
				$request .= ", " . $attributes[$i];
				$i++;
			}
			mysql_query($request)
				or myDie($currNode,'Cannot insert node attributes: ' . mysql_error());
		}
		
		//children
		if (level<2 && $node->children) {
			foreach($node->children->children() as $child) {
				$currNode[$level+1]++;
				loadNode($child, $currNode, $level+1);
			}
		}
	}
	
	if ($_REQUEST['xml']) {
		$xml = $_REQUEST['xml'];
		if (get_magic_quotes_gpc()) {
			$xml = stripslashes($xml);
		}
		$experiment = simplexml_load_string($xml);
		if (!$experiment) {
			echo "failed loading XML\n";
			foreach(libxml_get_errors() as $error) {
				echo "\t", $error->message;
			}
			die();
		}
		foreach($experiment->attributes[0]->attribute as $attribute) {
			switch((string)$attribute[KEY_NAME]) {
				case NAME_CODE:
					$code = $attribute[KEY_VALUE];
					break;
			}
		}
		if ($code) {
			mysql_query("INSERT INTO experiments (code) VALUES ('".mysql_real_escape_string($code)."')")
				or die('Creating experiment failed: ' . mysql_error());
		} else {
			die('Experiment code not found');
		}
		loadNode($experiment, array(mysql_insert_id(), 0, 0));
		echo "Done.";
	} else {
?>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	</head>
	<body>
		<form method="post" action="addexperiment.php">
			XML:<br />
			<input name="xml"><br />
			<input type="submit" value="Abschicken" />
		</form>
	</body>
</html>
<?
 }
?>