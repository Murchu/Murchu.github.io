var gulp = require('gulp');
var browserSync = require('browser-sync').create();
var reload = browserSync.reload;


var $ = require("gulp-load-plugins")({
  		pattern: ['gulp-*', 'gulp.*'], 
    	rename : { 'gulp-clean-css'  : 'cleancss', 'gulp-svgmin' : 'svgmin' }   	//Slight workaround
});
	
var dist = {
 	css: 			'dist/css',
 	appCss: 		'app/css', // Not for dist but returns compiled css for debugging
 	js:  			'dist/js',
 	img: 			'dist/img',
 	svg: 			'dist/img/svg',
 	html: 			'',
 	tempDest: 		'dist/templates',
 	partialDest: 	'dist/partials',
};

var paths = {
	sass: 			'app/scss/**/*.scss',
	css: 			'app/css/*.css',
	js: 			'app/js/**/*.js',
	img: 			'app/images/*',
	svg: 			'app/images/svg/*.svg',
	html: 			'app/pages/*.html',
	templates: 		'app/templates/*.html',
	partials: 		'app/partials/*.html',
	compiledCss: 	'dist/css/*.css', 
}

function errorLog (error){
	console.error.bind(error);
	this.emit('end');
}


// Sass Task -Compiled Sass code
gulp.task('sass', function(){
	gulp.src(paths.sass)
		.pipe($.sass({ outputStyle: 'expanded' }) // nested , compact, compressed , expanded		
		.on('error', $.sass.logError))	
		.pipe($.autoprefixer({
            browsers: ['last 2 versions'],
            cascade: false
        }))
		.pipe(gulp.dest(dist.appCss)) // just piping back to original app	
		.pipe(gulp.dest(dist.css))
		.pipe($.cleancss({compatibility: 'ie8'}))
 		.pipe($.rename({
      		suffix: '.min'
    	}))   	
		.pipe(gulp.dest(dist.css))		
		.pipe(browserSync.reload( { stream: true } ));   
});﻿


// Scripts Task - Minify files with UglifyJS.
gulp.task('scripts', function(){
	return gulp.src(paths.js)
		.pipe($.uglify()).on('error', errorLog)
		 .pipe($.rename({
      		suffix: '.min'
    	}))
		.pipe(gulp.dest(dist.js))
		.pipe(browserSync.reload( { stream: true } ));   
});


// Image Task - Minify images
gulp.task('image', function(){
	gulp.src(paths.img)
		.pipe($.imagemin({
            progressive: true
        }))
		.on('error', errorLog)
		.pipe(gulp.dest( dist.img));

});


// SVG Task - Optimize SVG
gulp.task('svg', function () {
    gulp.src(paths.svg)
        .pipe($.svgmin())
        .pipe(gulp.dest(dist.svg));
});


// minifiy Main HTML - gulp plugin to minify HTML. 
gulp.task('htmlmin', function() {
	gulp.src( paths.html )
		.pipe($.htmlmin({collapseWhitespace: true}))
		.pipe(gulp.dest(dist.html))
		.pipe(browserSync.reload( { stream: true } ));   
});


// minifiy templates HTML - gulp plugin to minify HTML. 
gulp.task('temphtmlmin', function() {
	gulp.src( paths.templates )
		.pipe($.htmlmin({collapseWhitespace: true}))
		.pipe(gulp.dest(dist.tempDest))
		.pipe(browserSync.reload( { stream: true } ));   
});


// minifiy templates HTML - gulp plugin to minify HTML. 
gulp.task('partialhtmlmin', function() {
	gulp.src( paths.partials )
		.pipe($.htmlmin({collapseWhitespace: true}))
		.pipe(gulp.dest(dist.partialDest))
		.pipe(browserSync.reload( { stream: true } ));   
});

// Watch Task - Watch files for change
gulp.task('watch',  function(){		
		gulp.watch(paths.img, 	['image']);
});



// browserSync
gulp.task('browserSync', function() {

	browserSync.init({
		server: {
			baseDir: '',
			port: 3001
		}
	});

	gulp.watch( paths.sass, ['sass']);
	gulp.watch( paths.html, ['htmlmin']);
	gulp.watch( paths.partials, ['partialhtmlmin']);
	gulp.watch( paths.templates, ['temphtmlmin']);
	gulp.watch( paths.html, ['htmlmin']);
	gulp.watch( paths.js, 	['scripts']);
});



// Default gulp task
gulp.task('default', [ 'watch', 'scripts', 'sass', 'image', 'svg', 'partialhtmlmin', 'temphtmlmin', 'htmlmin', 'browserSync']);

