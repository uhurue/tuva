#!/usr/bin/perl
use Regexp::Common qw /net/;
use Time::Local;

my $ret;

&UTC2Localtime("2015-03-20 00:54:38 UTC");

$ret = &time_compare("2015-03-20 00:54:38", "2015-03-19 00:32:38");
printf "return = $ret\n";

$ret = &time_compare("2015-03-19 11:54:38", "2015-03-19 17:32:38");
printf "return = $ret\n";

printf &get_localtime();

# UTC --> localtime
sub UTC2Localtime
{
	my $time_str = shift;
	my ($year, $month, $day, $hour, $extra) = split(/:|-| /, $time_str, 5);
	printf "year=$year month=$month day=$day hour=$hour, extra=$extra\n";
}

# if master is bigger(recent) then positive seconds
sub time_compare
{
	my $master_time = shift;
	my $sub_time = shift;
	my $master_epoch;
	my $sub_epoch;
	my ($year, $month, $day, $hour, $min, $sec) = split(/:|-| /, $master_time, 6);
	$master_epoch = timelocal($sec, $min, $hour, $day, $month-1, $year);
	($year, $month, $day, $hour, $min, $sec) = split(/:|-| /, $sub_time, 6);
	$sub_epoch = timelocal($sec, $min, $hour, $day, $month-1, $year);
	return ($master_epoch - $sub_epoch);
}

sub get_localtime
{
	my ($sec,$min,$hour,$mday,$mon,$year,$wday,$yday,$isdst) = localtime(time);
	return (sprintf("%04d-%02d-%02d %02d:%02d:%02d", $year+1900, $mon+1, $mday, $hour, $min, $sec));
}