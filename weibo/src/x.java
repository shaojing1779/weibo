		String csv = "";
	    	String str = "";
            	List<String> newList = new ArrayList<String>();
            	for(Text val:values){   //val变量用于包
        			str = val.toString();
                	if(newList.contains(str)){
                   	if(csv.length()>0) csv += ",";
                    csv += str;
                }
                else{
                    newList.add(str);
                }
                
            }
	    newList.clear();
	    if(!csv.equals("")) context.write(key, new Text(csv));
        }
