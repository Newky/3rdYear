#!/usr/bin/perl
use strict;
use warnings;

my $html_file = shift;
my $mode = shift;

if($mode){
	open (F_HTML, "<", $html_file)  or  die "Failed to read file $html_file : $!";
	while (<F_HTML>) {
		my $file = $_;
		$file =~ s/(<.*?>)/$1\n/g;
		$file =~ s/(<a .*?>)\n/$1/g;
		print $file;
	}
	close (F_HTML);
}
else{
	open (F_HTML, "<", $html_file)  or  die "Failed to read file $html_file : $!";
	while (<F_HTML>) {	
		if(m/<a .*?>/){
			my $file = $_;
			$file =~ m/<a .*? href=(".*?")>(.*?)<\/a>/;
			my $url = $1;
			my $blurb = $2;
			my $anchor = sprintf "<a href=%s>%s</a>", $url, $blurb;
			printf "%s<SEP>%s\n" , $blurb, $anchor;
		}
	}
	close (F_HTML);
}
