<?php

// make sure browsers see this page as utf-8 encoded HTML
header('Content-Type: text/html; charset=utf-8');

$limit = 10;
$query = isset($_REQUEST['q']) ? $_REQUEST['q'] : false;
$rankAlgorithm = isset($_GET['rankAlgorithm']) ? $_GET['rankAlgorithm'] : false;
$results = false;
$lucenePagerankParameters = array(
  'fl' => 'title,og_url,og_description,id'
);
$networkxPagerankParameters = array(
  'fl' => 'title,og_url,og_description,id',
  'sort' => 'pageRankFile desc'
);


if ($query)
{
  // The Apache Solr Client library should be on the include path
  // which is usually most easily accomplished by placing in the
  // same directory as this script ( . or current directory is a default
  // php include path entry in the php.ini)
  require_once('Apache/Solr/Service.php');

  // create a new solr service instance - host, port, and webapp
  // path (all defaults in this example)
  $solr = new Apache_Solr_Service('localhost', 8983, '/solr/myexample');

  // if magic quotes is enabled then stripslashes will be needed
  if (get_magic_quotes_gpc() == 1)
  {
    $query = stripslashes($query);
  }

  // in production code you'll always want to use a try /catch for any
  // possible exceptions emitted  by searching (i.e. connection
  // problems or a query parsing error)
  try
  {
    if ($rankAlgorithm == "lucene") {
      $results = $solr->search($query, 0, $limit, $lucenePagerankParameters);
    } else {
      $results = $solr->search($query, 0, $limit, $networkxPagerankParameters);
    }
  }
  catch (Exception $e)
  {
    // in production you'd probably log or email this error to an admin
    // and then show a special message to the user but for this example
    // we're going to show the full exception
    die("<html><head><title>SEARCH EXCEPTION</title><body><pre>{$e->__toString()}</pre></body></html>");
  }
}

?>
<html>
  <head>
    <title>Qixiong Liu HW4</title>
    <link rel="stylesheet" href="https://cdn.bootcss.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <style type="text/css">
      #title {
        font-size: 16px;
      }
      #url {
        font-size: 14px;
      }
      #description {
        font-size: 14px;
      }
      #id {
        font-size: 14px;
        color:grey;
      }

    </style>
  </head>
  <body>
    <div class="container">
      <div style="height: 20px;"></div>
      <form  accept-charset="utf-8" method="get">
        <div class="form-group row">
          <label class="col-form-label" for="q">Search:</label>
          <div class="col-xl-4">
            <input class="form-control" id="q" name="q" type="text" value="<?php echo htmlspecialchars($query, ENT_QUOTES, 'utf-8'); ?>"/>
          </div>
          <div class="col-xl-6">
            <input class="btn btn-outline-secondary" id="radio1" name="rankAlgorithm" type="radio" <?php if($rankAlgorithm != "networkx") { echo "checked='checked'"; } ?> value="lucene" /> 
              <label class="form-check-label" for="exampleRadios2">
                Lucene&nbsp;&nbsp;
              </label>
            <input class="btn btn-outline-secondary" id="radio2" name="rankAlgorithm" type="radio" <?php if($rankAlgorithm == "networkx") { echo "checked='checked'"; } ?> value="networkx" />
              <label class="form-check-label" for="exampleRadios2">
                NetworkX&nbsp;&nbsp;
              </label>
            <input class="btn btn-outline-secondary" type="submit"/>
          </div>
        </div>
      </form>
<?php

// display results
if ($results)
{
  $total = (int) $results->response->numFound;
  $start = min(1, $total);
  $end = min($limit, $total);

  // get the urlId mapfile from csv
  $urlArray = array();
  // $file = fopen("UrlToHtml_NBCNews.csv", "r");
  // while (! feof($file)) {
  //   $input = fgetcsv($file);
  //   $fileId = $input[0];
  //   $fileUrl = $input[1];
  //   $fileId = "/Users/qixiongliu/Desktop/Graduate/CSCI572/HW/HW4/DataSet/NBC_News/crawl_data/".$fileId;
  //   $tempArray = array($fileId => $fileUrl);
  //   $urlArray = array_merge($urlArray,$tempArray);
  // }
  // fclose($file);
  function getUrlFromCSV() {
    $resultArray = array();
    set_time_limit(1000);
    if (($handle = fopen("UrlToHtml_NBCNews.csv", "r")) !== false) {
      while (($data = fgetcsv($handle, 1000, ",")) !== FALSE) {
        $fileId = $data[0];
        $fileUrl = $data[1];
        $fileId = "/Users/qixiongliu/Desktop/Graduate/CSCI572/HW/HW4/DataSet/NBC_News/crawl_data/".$fileId;
        $tempArray = array($fileId => $fileUrl);
        $resultArray = array_merge($resultArray,$tempArray);
      }
    }
    fclose($handle);
    // print_r("Done");
    return $resultArray;
  }
?>
    <div>Results <?php echo $start; ?> - <?php echo $end;?> of <?php echo $total; ?>:</div>
    <div style="height:10px;"></div>
    <div class="row">
      <div class="col-xl-9">
        <ol class="list-group" style="list-style-type:none;">
<?php
  // iterate result documents
  foreach ($results->response->docs as $doc)
  {
?>
          <li>
            <table style="border: 0px solid black; text-align: left;">
<!-- <?php
    // iterate document fields / values
    // foreach ($doc as $field => $value)
    {
?> -->    
          <?php
            $id = $doc->id;
            if (isset($doc->og_url)) {
              $url = $doc->og_url;
            } else {
              if (sizeof($urlArray) == 0) {
                $urlArray = getUrlFromCSV($urlArray);
                $url = $urlArray[$id];
              } else {
                $url = $urlArray[$id];
              }
            }
            
          ?>
              <!-- title -->
              <tr>
                <!-- <th>title</th> -->
                <!-- <th><?php // echo htmlspecialchars($field, ENT_NOQUOTES, 'utf-8'); ?></th> -->
                <td>
                  <a target="_blank" href="<?php echo htmlspecialchars($url, ENT_NOQUOTES, 'utf-8'); ?>">
                    <span id="title">
                      <?php
                        if (isset($doc->title)) {
                          echo htmlspecialchars($doc->title, ENT_NOQUOTES, 'utf-8'); 
                        } else {
                          echo "NA";
                        }
                      ?>
                    </span>
                  </a>
                </td>
              </tr>
              <!-- url -->
              <tr>
                <td>
                  <a target="_blank" href="<?php echo htmlspecialchars($url, ENT_NOQUOTES, 'utf-8'); ?>">
                    <span id="url">
                      <?php
                        echo htmlspecialchars($url, ENT_NOQUOTES, 'utf-8');
                      ?>
                    </span>
                  </a>
                </td>
              </tr>
              <!-- description -->
              <tr>
                <td>
                  <span id="description">
                    <?php
                      if (isset($doc->og_description)) {
                        echo htmlspecialchars($doc->og_description, ENT_NOQUOTES, 'utf-8');
                      } else {
                        echo "NA";
                      }
                    ?>
                  </span>
                </td>
              </tr>
              <!-- id -->
              <tr>
                <td>
                  <span id="id">
                    <?php
                      echo htmlspecialchars($id, ENT_NOQUOTES, 'utf-8');
                    ?>
                  </span>
                </td>
              </tr>
<!-- <?php
    }
?> -->
            </table>
          </li>
          <div style="height:20px;"></div>
<?php
  }
?>
        </ol>
      </div>
    <div class="col-xl-3"></div>
  </div>
<?php
}
?>
    </div>
  </body>
</html>