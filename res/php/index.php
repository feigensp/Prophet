<?php	
	require_once('config.php');
	require_once('constants.php');	
	
	header("Content-Type: text/html; charset=UTF-8");

	echo HTML_START;
	
	function myEnd($error = "") {
		if ($error) {
			echo "<p>Error: $error</p>";
		}
		echo HTML_END;
		die();
	}
	
	function myStripSlashes($string) {
		if (get_magic_quotes_gpc()) {
			return stripslashes($string);
		}
		return $string;
	}
	function myAddSlashes($string) {
		return mysql_real_escape_string($string);
	}
	function mySlashes($string) {
		return myAddSlashes(myStripSlashes($string));
	}
	
	function getNextNodes($experiment,$category,$question) {
	//Request for next non-inactive, non-donotshowcontent questions and categories in any non-inactive category
	$request = <<<SQL_REQUEST
SELECT nodes.experiment, nodes.category, nodes.question
FROM nodes
JOIN (
	SELECT cat.experiment, cat.category
	FROM nodes cat
	LEFT JOIN nodes_attributes catattr ON cat.experiment = catattr.experiment
	AND cat.category = catattr.category
	AND cat.question = catattr.question
	AND catattr.name = "inactive"
	WHERE cat.question =0
	AND (
		catattr.value IS NULL
		OR catattr.value =0
	)
)cat_not_inactive ON nodes.experiment = cat_not_inactive.experiment
AND nodes.category = cat_not_inactive.category
LEFT JOIN nodes_attributes attr ON nodes.experiment = attr.experiment
AND nodes.category = attr.category
AND nodes.question = attr.question
AND attr.name = "inactive"
LEFT JOIN nodes_attributes attr2 ON nodes.experiment = attr2.experiment
AND nodes.category = attr2.category
AND nodes.question = attr2.question
AND attr2.name = "donotshowcontent"
WHERE nodes.experiment =$experiment
AND (
	(
		nodes.category =$category
		AND nodes.question >$question
	)
	OR nodes.category >$category
)
AND (
	attr.value IS NULL
	OR attr.value =0
)
AND (
	attr2.value IS NULL
	OR attr2.value =0
)
ORDER BY nodes.experiment ASC , nodes.category ASC , nodes.question ASC
SQL_REQUEST;
		$result = mysql_query($request)
			or myEnd('Cannot retrieve next node: ' . mysql_error());
		while ($line = mysql_fetch_array($result)) {
			$lines[] = $line;
		}
		return $lines;
	}
	
	if ($_REQUEST[KEY_EXPERIMENT_CODE]) {
		//echo "DEBUG: Received experiment code";
		$experiment_code = mySlashes($_REQUEST[KEY_EXPERIMENT_CODE]);
		
		//Show experiment start page
		$result = mysql_query("SELECT experiment FROM experiments WHERE code=\"$experiment_code\"")
			or myEnd('Cannot retrieve experiment: ' . mysql_error());
		$experiment_row=mysql_fetch_row($result);
		if (!$experiment_row) {
			myEnd('Experiment not found.');
		}
		$experiment=$experiment_row[0];
		$category=0;
		$question=0;
		$showNode=1;
	}
	if(is_numeric($_POST[KEY_EXPERIMENT]) && is_numeric($_POST[KEY_CATEGORY]) && is_numeric($_POST[KEY_QUESTION])) {
		$experiment=mySlashes($_POST[KEY_EXPERIMENT]);
		$category=mySlashes($_POST[KEY_CATEGORY]);
		$question=mySlashes($_POST[KEY_QUESTION]);		
		//echo "DEBUG: Received node codes: $experiment,$category,$question";
		$timeused=myAddSlashes(microtime(true)-myStripSlashes($_POST[KEY_TIME]));
		
		$result = mysql_query("SELECT * FROM experiments WHERE experiment=\"$experiment\"")
			or myEnd('Cannot retrieve experiment: ' . mysql_error());
		$experiment_row=mysql_fetch_row($result);
		if (!$experiment_row) {
			myEnd('Experiment not found.');
		}
		
		//read and save values
		if ($_POST[KEY_SUBJECT]) {
			//subject id exists
			//echo "DEBUG: Received subject";
			$subject=mySlashes($_POST[KEY_SUBJECT]);
		} else {
			//create subject
			if ($_POST[KEY_SUBJECT_CODE]) {
				//echo "DEBUG: Creating subject";			
				//Create subject in database
				$subject_code = mySlashes($_POST[KEY_SUBJECT_CODE]);
				$result = mysql_query("SELECT subject FROM subjects WHERE experiment=\"$experiment\" AND code=\"$subject_code\"")
					or myEnd('Cannot retrieve subject: ' . mysql_error());
				$subject_row=mysql_fetch_row($result);
				if ($subject_row) {
					echo "<p><b>A subject with this code already exists.</b></p>";
				} else {
					mysql_query("INSERT INTO subjects (experiment,code) VALUES (\"$experiment\",\"$subject_code\")")
						or myEnd('Cannot create subject: ' . mysql_error());
					$subject = mysql_insert_id();
				}
			} else {
				echo "<p><b>Please enter a subject code.</b></p>";
			}
		}
		if(!$subject) {
			//Happens if subject fails to enter new and valid subject code
			//echo "DEBUG: Do not have subject id";
			$category=0;
			$question=0;
			$showNode=1;
		} else {
			//Write answers if they don't yet exist
			//echo "DEBUG: Writing answers";
			$result = mysql_query("SELECT * FROM nodes WHERE experiment=\"$experiment\" AND category=\"$category\" AND question=\"$question\"")
				or myEnd('Cannot retrieve node: ' . mysql_error());
			$experiment_row=mysql_fetch_row($result);
			if (!$experiment_row) {
				myEnd('Node not found.');
			}
		
			$result = mysql_query("SELECT * FROM answers WHERE experiment=\"$experiment\" AND category=\"$category\" AND question=\"$question\" AND subject=\"$subject\"")
				or myEnd('Cannot retrieve answers: ' . mysql_eror());
			if (mysql_fetch_row($result)) {
				echo "<p><b>Your answers have already been saved, ignoring last input.</b></p>";
			} else {
				foreach($_POST as $name => $value) {
					if ($name!=KEY_EXPERIMENT &&
						$name!=KEY_CATEGORY &&
						$name!=KEY_QUESTION &&
						$name!=KEY_SUBJECT &&					
						$name!=KEY_EXPERIMENT_CODE &&
						$name!=KEY_SUBJECT_CODE &&
						$name!=KEY_TIME &&
						$name!=KEY_FORWARD) {
						$answers[]="(\"$experiment\",\"$category\",\"$question\",\"$subject\",\"$name\",\"$value\")";
					}
				}
				//insert answer node
				mysql_query("INSERT INTO answers (experiment,category,question,subject,time) VALUES (\"$experiment\",\"$category\",\"$question\",\"$subject\",\"$timeused\")")
					or myEnd('Cannot insert answer node: ' . mysql_error());
				//insert answer attributes
				if ($answers) {
					$request = "INSERT INTO answers_attributes (experiment, category, question, subject, name, value) VALUES ".$answers[0];
					$i = 1;
					while ($answers[$i]) {						
						$request .= ", " . $answers[$i];
						$i++;
					}
					mysql_query($request)
						or myEnd('Cannot insert answer attributes: ' . mysql_error());
				}
			}
			//Find next node
			//echo "DEBUG: Searching next node";
			$nextNodes = getNextNodes($experiment,$category,$question);
			//var_dump($nextNodes);
			if (!$nextNodes) {
				//End questionnaire
				echo "Questionnaire finished.";
				myEnd();
			}
			$nextNode = $nextNodes[0];
			//var_dump($nextNode);
			if (!$nextNodes[1] or $nextNodes[1]['category']>$nextNode['category']) {
				//echo "DEBUG: Next node ends category";
				$endsCategory=true;
			}
			$experiment=$nextNode['experiment'];
			$category=$nextNode['category'];
			$question=$nextNode['question'];
			$showNode=1;
		}
	}		
	if($showNode) {
		//echo "DEBUG: Next/this node: $experiment,$category,$question";
		$result = mysql_query("SELECT * FROM nodes WHERE experiment=\"$experiment\" AND category=\"$category\" AND question=\"$question\"")
			or myEnd('Cannot retrieve node: ' . mysql_error());
		$result=mysql_fetch_array($result);
		if (!result) {
			myEnd('Node not found: $experiment,$category,$question');
		}
		$name=$result['name'];
		$value=$result['value'];
		$startsExperiment = ($category==0 && $question==0);
		//remember: $endsCategory, $subject (if not $startsExperiment)
		echo "<input type=\"hidden\" name=\"".KEY_TIME."\" value=\"".microtime(true)."\" />";
		echo "<input type=\"hidden\" name=\"".KEY_EXPERIMENT."\" value=\"$experiment\" />";
		echo "<input type=\"hidden\" name=\"".KEY_CATEGORY."\" value=\"$category\" />";
		echo "<input type=\"hidden\" name=\"".KEY_QUESTION."\" value=\"$question\" />";
		if ($category!=0 || $question!=0) {
			echo "<input type=\"hidden\" name=\"".KEY_SUBJECT."\" value=\"$subject\" />";
		}
		echo "<h1>$name</h1><br /><br />";
		echo $value;
		echo HTML_DIVIDER;
		if ($startsExperiment) {
			echo FOOTER_START_EXPERIMENT;
		} else if ($endsCategory) {
			echo FOOTER_END_CATEGORY;
		} else {
			echo FOOTER_FORWARD;
		}
		myEnd();
	} else {		
		//echo "DEBUG: No node to show";
		echo "Experiment-Code:<br />";
		echo "<input name=\"".KEY_EXPERIMENT_CODE."\"><br />";
		echo "<input type=\"submit\" value=\"Abschicken\" />";
		myEnd();
	}
?>