<?php

//change the location of the php file on the next line!!!
require_once 'C:/wamp/www/twitter-php-3.5/src/twitter.class.php';

$consumerKey = $_GET['consumerKey'];
$consumerSecret = $_GET['consumerSecret'];
$accessToken = $_GET['accessToken'];
$accessTokenSecret = $_GET['accessTokenSecret'];
$statusMsg = $_GET['msg'];
$twitter = new Twitter($consumerKey, $consumerSecret, $accessToken, $accessTokenSecret);

try {
	$tweet = $twitter->send($statusMsg);

} catch (TwitterException $e) {
	echo 'Error: ' . $e->getMessage();
}
?>