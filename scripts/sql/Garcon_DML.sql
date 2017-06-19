-- INSERT BASELINE CLUSTER DATA
INSERT INTO `GARCON`.`NIFI_CLUSTER` (`CLUSTER_ID`, `CLUSTER_NAME`, `CLUSTER_DESC`, `CLUSTER_HOSTNAME`, `CLUSTER_PORT`) VALUES ('1', 'WhiskeyCellar', 'Unbuntu local home network testing server', '192.168.1.75', '8080');
INSERT INTO `GARCON`.`NIFI_CLUSTER` (`CLUSTER_ID`, `CLUSTER_NAME`, `CLUSTER_DESC`, `CLUSTER_HOSTNAME`, `CLUSTER_PORT`) VALUES ('2', 'JDyer FieldCloud', 'Fieldcloud instance for testing', 'jdyer-garcon0.field.hortonworks.com', '8080');

-- INSERT BASELINE MINIFI DEVICES
INSERT INTO `GARCON`.`MINIFI_NODE` (`NODE_ID`, `INTERNAL_IP`, `EXTERNAL_IP`, `HOSTNAME`, `NUM_PROCESSORS`, `DEVICE_NAME`, `DEVICE_TYPE`) VALUE ('1', '192.168.1.75', '192.168.1.75', 'whiskeycellar', '4', 'WhiskeyCellar Ubuntu 16.04', 'c++');

-- INSERT SOME SAMPLE METRIC THRESHOLDS.
INSERT INTO `GARCON`.`METRIC_THRESHOLD` (`THRESHOLD_ID`, `CLUSTER_ID`, `THRESHOLD_NAME`, `THRESHOLD_DESC`, `COMPONENT_ID`, `API_URI`) VALUES ('1', '2', 'Connection Depth', 'Watch for connections with more than 10 flowfiles', 'root', '/nifi-api/process-groups/root/connections');